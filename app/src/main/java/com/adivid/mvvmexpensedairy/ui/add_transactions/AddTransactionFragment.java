package com.adivid.mvvmexpensedairy.ui.add_transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentAddTransactionBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.KEY_DATE_DISPLAY_FORMAT;

@AndroidEntryPoint
public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;

    private String stringDate, stringTime, stringDbDate, stringAmount, stringTransactionType,
            stringCategoryType, stringNote, stringPaymentType;
    private Calendar calendar;
    private int mDay, mMonth, mYear;
    private List<String> listCategory;
    private AddTransactionViewModel viewModel;

    public AddTransactionFragment() {
        super(R.layout.fragment_add_transaction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddTransactionBinding.bind(view);

        init();
        setUpOnClickListeners();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);
        setUpDateAndTime();
        listCategory = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.category_arr)));
        stringTransactionType = "Expense";
        stringCategoryType = "Others";
        calendar = Calendar.getInstance();
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMonth = calendar.get(Calendar.MONTH);
        mYear = calendar.get(Calendar.YEAR);

        addDynamicChips();

    }

    private void addDynamicChips() {
        /*Chip chip = new Chip(requireContext());
        chip.setText("Try");
        binding.chipGroupPaymentType.addView(chip);*/
    }

    private void setUpOnClickListeners() {
        binding.ivBack.setOnClickListener(v -> {

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
            switch (checkedId){
                case R.id.chip_expense:
                    stringTransactionType = "Expense";
                    Timber.d("chip_expense");
                    break;
                case R.id.chip_income:
                    stringTransactionType = "Income";
                    Timber.d("chip_income");
                    break;
            }
        });

        binding.chipGroupPaymentType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.chip_cash:
                    stringTransactionType = "Cash";
                    Timber.d("chip_expense");
                    break;
                case R.id.chip_card:
                    stringTransactionType = "Income";
                    Timber.d("chip_income");
                    break;
            }
        });
    }

    private void saveInDb() {
        ExpenseEntity expenseEntity = new ExpenseEntity(
                stringDbDate, stringTime, stringAmount, stringTransactionType,
                stringCategoryType, stringNote, String.valueOf(System.currentTimeMillis())
        );
        viewModel.insertTransaction(expenseEntity);
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(KEY_DATE_DISPLAY_FORMAT,
                            Locale.getDefault());
                    binding.etDate.setText(dateFormat.format(newDate.getTime()));

                    Timber.d("day: " + newDate.get(Calendar.DAY_OF_MONTH));
                    Timber.d("month: " + newDate.get(Calendar.MONTH));
                    Timber.d("year: " + newDate.get(Calendar.YEAR));

                    mDay = newDate.get(Calendar.DAY_OF_MONTH);
                    mMonth = newDate.get(Calendar.MONTH);
                    mYear = newDate.get(Calendar.YEAR);
                }, mYear, mMonth , mDay).show();

    }

    private boolean validateFields() {
        stringDbDate = Utils.convertToStoringDate(binding.etDate.getText().toString());
        stringAmount = binding.etAmount.getText().toString().trim();
        stringNote = binding.etNote.getText().toString();
        stringPaymentType = "Cash";

        if (stringAmount.isEmpty()) {
            binding.etAmount.setError("Please Enter Amount");
            binding.etAmount.requestFocus();
            return false;
        } else if (stringNote.isEmpty()) {
            binding.etNote.setError("Please Enter Note");
            binding.etNote.requestFocus();
            return false;
        }
        return true;
    }

    private void setUpDateAndTime() {
        stringDate = Utils.getDisplayDate();
        stringTime = Utils.getCurrentTime();
        binding.etDate.setText(stringDate);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
