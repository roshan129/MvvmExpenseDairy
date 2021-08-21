package com.roshanadke.mvvmexpensedairy.ui.add_transactions;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.roshanadke.mvvmexpensedairy.R;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.databinding.FragmentAddTransactionBinding;
import com.roshanadke.mvvmexpensedairy.utils.DataSyncWorker;
import com.roshanadke.mvvmexpensedairy.utils.UpdateDataSyncWorker;
import com.roshanadke.mvvmexpensedairy.utils.Utils;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_DATE_DISPLAY_FORMAT;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_UNIQUE_WORK;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_UPDATE_UNIQUE_WORK;

@AndroidEntryPoint
public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;

    private String stringDate, stringTime, stringAmount, stringTransactionType,
            stringCategoryType, stringNote, stringPaymentType;
    private int mDay, mMonth, mYear;
    private List<String> listCategory;
    private AddTransactionViewModel viewModel;

    private ArrayAdapter<String> arrayAdapterCategory;
    private int updateId;

    public Date storingDate;

    @Inject
    public FirebaseAuth firebaseAuth;

    private ExpenseEntity existingExpenseEntity;

    public AddTransactionFragment() {
        super(R.layout.fragment_add_transaction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddTransactionBinding.bind(view);

        Timber.d("int ; " + updateId);
        init();
        setUpAdapters();
        setUpOnClickListeners();
        getExtras();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);
        setUpDateAndTime();
        listCategory = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.category_arr_exp)));
        stringTransactionType = "Expense";
        stringCategoryType = "Others";
        stringPaymentType = "Cash";
        Calendar calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);

        addDynamicChips();
        binding.etAmount.requestFocus();
        showOrHideKeyBoard(true);
    }

    private void getExtras() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            existingExpenseEntity = (ExpenseEntity) bundle.getSerializable(EXPENSE_BUNDLE_KEY);
            if (existingExpenseEntity != null) {
                setUpData(existingExpenseEntity);
            }
        }
    }

    private void setUpData(ExpenseEntity expenseEntity) {
        if (expenseEntity.getNote().equals("Not Specified")) {
            binding.etNote.setText("");
        } else {
            binding.etNote.setText(expenseEntity.getNote());
        }
        binding.etAmount.setText(expenseEntity.getAmount());
        binding.etDate.setText(Utils.convertToDisplayDate(expenseEntity.getDate()));
        if (expenseEntity.getTransaction_type().equals("Income")) {
            binding.chipIncome.setChecked(true);
            listCategory.addAll(new ArrayList<>(Arrays.asList(
                    getResources().getStringArray(R.array.category_arr_exp))));
            arrayAdapterCategory.notifyDataSetChanged();
        }
        binding.spinnerCategory.setSelection(listCategory.indexOf(
                expenseEntity.getTransaction_category()));
        if (expenseEntity.getPayment_type().equals("Card")) {
            binding.chipCard.setChecked(true);
        }
        updateId = expenseEntity.getId();
    }

    private void setUpAdapters() {
        arrayAdapterCategory = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listCategory);
        binding.spinnerCategory.setAdapter(arrayAdapterCategory);

    }

    private void addDynamicChips() {
        /*Chip chip = new Chip(requireContext());
        chip.setText("Try");
        binding.chipGroupPaymentType.addView(chip);*/
    }

    private void setUpOnClickListeners() {
        binding.ivBack.setOnClickListener(v -> {
            showOrHideKeyBoard(false);
            requireActivity().onBackPressed();
        });

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringCategoryType = listCategory.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.fabSave.setOnClickListener(v -> {
            if (validateFields()) {
                saveInDb();
            }
        });

        binding.etDate.setOnClickListener(v -> showDatePickerDialog());

        binding.etTime.setOnClickListener(v -> showMaterialTimePicker());

        binding.chipGroupTransactionType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.chip_expense:
                    stringTransactionType = "Expense";
                    List<String> list = new ArrayList<>(Arrays.asList(
                            getResources().getStringArray(R.array.category_arr_exp)));
                    listCategory.clear();
                    listCategory.addAll(list);
                    binding.spinnerCategory.setSelection(0);
                    arrayAdapterCategory.notifyDataSetChanged();
                    break;
                case R.id.chip_income:
                    Timber.d("income");
                    stringTransactionType = "Income";
                    List<String> list1 = new ArrayList<>(Arrays.asList(
                            getResources().getStringArray(R.array.category_arr_inc)));
                    listCategory.clear();
                    listCategory.addAll(list1);
                    binding.spinnerCategory.setSelection(0);
                    arrayAdapterCategory.notifyDataSetChanged();
                    break;
            }
        });

        binding.chipGroupPaymentType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.chip_cash:
                    stringPaymentType = "Cash";
                    Timber.d("chip_expense");
                    break;
                case R.id.chip_card:
                    stringPaymentType = "Card";
                    Timber.d("chip_income");
                    break;
            }
        });
    }

    private void saveInDb() {
        ExpenseEntity expenseEntity = new ExpenseEntity(
                storingDate, stringTime, stringAmount, stringTransactionType,
                stringCategoryType, stringNote, stringPaymentType, String.valueOf(System.currentTimeMillis())
        );
        if (updateId != 0) {
            expenseEntity.setId(updateId);
            expenseEntity.setDataSent(false);
            expenseEntity.setUpdated(true);
            expenseEntity.setDocId(existingExpenseEntity.getDocId());
            expenseEntity.setFirebaseUid(existingExpenseEntity.getFirebaseUid());
            viewModel.updateTransaction(expenseEntity);
            syncUpdatedDataToServer();
        } else {
            viewModel.insertTransaction(expenseEntity);
            syncOfflineDataToServer();
        }
        showOrHideKeyBoard(false);
        requireActivity().onBackPressed();

    }

    private void showMaterialTimePicker() {
        String time = binding.etTime.getText().toString();
        int hour = Integer.parseInt(time.substring(0, time.indexOf(":")));
        int minute = Integer.parseInt(time.substring(time.indexOf(":") + 1));
        Timber.d("hour: " + hour + " minute: " + minute);

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hour)
                .setMinute(minute)
                .build();
        materialTimePicker.show(getChildFragmentManager(), null);
        materialTimePicker.addOnPositiveButtonClickListener(v -> {
            stringTime = materialTimePicker.getHour() + ":" + materialTimePicker.getMinute();
            binding.etTime.setText(stringTime);
        });
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(KEY_DATE_DISPLAY_FORMAT,
                            Locale.getDefault());
                    binding.etDate.setText(dateFormat.format(newDate.getTime()));

                    Timber.d("day: %s", newDate.get(Calendar.DAY_OF_MONTH));
                    Timber.d("month: %s", newDate.get(Calendar.MONTH));
                    Timber.d("year: %s", newDate.get(Calendar.YEAR));

                    mDay = newDate.get(Calendar.DAY_OF_MONTH);
                    mMonth = newDate.get(Calendar.MONTH);
                    mYear = newDate.get(Calendar.YEAR);
                }, mYear, mMonth, mDay).show();

    }

    private boolean validateFields() {
        storingDate = Utils.convertToStoringDate(binding.etDate.getText().toString(), stringTime);
        stringAmount = binding.etAmount.getText().toString().trim();
        stringNote = binding.etNote.getText().toString();

        if (stringAmount.isEmpty()) {
            binding.etAmount.setError("Please Enter Amount");
            binding.etAmount.requestFocus();
            return false;
        } else if (stringNote.isEmpty()) {
            stringNote = "Not Specified";
        }
        stringAmount = Utils.convertToDecimalFormat(stringAmount);
        return true;
    }

    private void syncOfflineDataToServer() {
        if (firebaseAuth.getCurrentUser() != null) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DataSyncWorker.class)
                    .setConstraints(constraints)
                    .addTag(KEY_UNIQUE_WORK)
                    .build();
            WorkManager.getInstance(requireContext()).enqueueUniqueWork(KEY_UNIQUE_WORK,
                    ExistingWorkPolicy.KEEP, request);
/*
            OneTimeWorkRequest updateRequest = new OneTimeWorkRequest.Builder(UpdateDataSyncWorker.class)
                    .setConstraints(constraints)
                    .addTag(KEY_UPDATE_UNIQUE_WORK)
                    .build();
            WorkManager.getInstance(requireContext()).enqueueUniqueWork(KEY_UPDATE_UNIQUE_WORK,
                    ExistingWorkPolicy.KEEP, updateRequest);*/
        }
    }

    private void syncUpdatedDataToServer(){
        if(firebaseAuth.getCurrentUser()!=null){
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest updateRequest = new OneTimeWorkRequest.Builder(UpdateDataSyncWorker.class)
                    .setConstraints(constraints)
                    .addTag(KEY_UPDATE_UNIQUE_WORK)
                    .build();
            WorkManager.getInstance(requireContext()).enqueueUniqueWork(KEY_UPDATE_UNIQUE_WORK,
                    ExistingWorkPolicy.KEEP, updateRequest);
        }
    }

    private void setUpDateAndTime() {
        stringDate = Utils.getDisplayDate();
        stringTime = Utils.getCurrentTime();
        binding.etDate.setText(stringDate);
        binding.etTime.setText(stringTime);
    }

    private void showOrHideKeyBoard(Boolean b) {
        InputMethodManager imm =
                (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b) {
            //imm.showSoftInput(binding.etAmount, InputMethodManager.SHOW_IMPLICIT);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            imm.hideSoftInputFromWindow(binding.etAmount.getWindowToken(), 0);
            //imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
