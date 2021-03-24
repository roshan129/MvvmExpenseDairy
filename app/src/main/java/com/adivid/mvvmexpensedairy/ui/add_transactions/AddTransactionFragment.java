package com.adivid.mvvmexpensedairy.ui.add_transactions;

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
import androidx.navigation.NavArgs;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentAddTransactionBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;
import static com.adivid.mvvmexpensedairy.utils.Constants.KEY_DATE_DISPLAY_FORMAT;

@AndroidEntryPoint
public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;

    private String stringDate, stringTime, stringAmount, stringTransactionType,
            stringCategoryType, stringNote, stringPaymentType;
    private Date storingDate;
    private Calendar calendar;
    private int mDay, mMonth, mYear;
    private List<String> listCategory;
    private AddTransactionViewModel viewModel;

    private NavArgs navArgs;

    private ArrayAdapter<String> arrayAdapterCategory;
    private int updateId;

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
        calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);

        addDynamicChips();

    }

    private void getExtras() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            ExpenseEntity expenseEntity = (ExpenseEntity) bundle.getSerializable(EXPENSE_BUNDLE_KEY);
            if (expenseEntity != null) {
                setUpData(expenseEntity);
            }
        }
    }

    private void setUpData(ExpenseEntity expenseEntity) {
        binding.etNote.setText(expenseEntity.getNote());
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
        if(expenseEntity.getPayment_type().equals("Card")){
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

        binding.etDate.setOnClickListener(v -> {
            showDatePickerDialog();
        });

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
        if(updateId != 0){
            expenseEntity.setId(updateId);
            viewModel.updateTransaction(expenseEntity);
        }else {
            viewModel.insertTransaction(expenseEntity);
        }
        hideKeyboard();
        requireActivity().onBackPressed();

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
        storingDate = Utils.convertToStoringDate(binding.etDate.getText().toString());
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

    private void setUpDateAndTime() {
        stringDate = Utils.getDisplayDate();
        stringTime = Utils.getCurrentTime();
        binding.etDate.setText(stringDate);
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
