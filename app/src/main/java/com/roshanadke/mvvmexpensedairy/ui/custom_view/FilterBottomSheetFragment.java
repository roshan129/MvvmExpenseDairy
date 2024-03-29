package com.roshanadke.mvvmexpensedairy.ui.custom_view;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.roshanadke.mvvmexpensedairy.R;
import com.roshanadke.mvvmexpensedairy.adapter.interfaces.FilterCallback;
import com.roshanadke.mvvmexpensedairy.databinding.FragmentFilterBottomSheetBinding;
import com.roshanadke.mvvmexpensedairy.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.BUNDLE_CATEGORY;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.BUNDLE_DATE_RANGE;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.BUNDLE_PAYMENT;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentFilterBottomSheetBinding binding;

    private List<String> listFilterDate;
    private String stringSpinnerDateFilter = "Select Date Range", stringFromDate, stringToDate,
            stringChipCategory = "All Categories", stringChipPayment = "All Payment Modes",
            stringSelectedMonth, stringSelectedYear;
    private int mYearFrom, mMonthFrom, mDayFrom, mYearTo, mMonthTo, mDayTo;

    private ArrayAdapter<String> arrayAdapterMonth, arrayAdapterYear;
    private String selectedDateRange;
    private FilterCallback filterCallback;

    //private MonthYearPickerDialogFragment dialogFragment;

    public FilterBottomSheetFragment() {
    }

    public FilterBottomSheetFragment(FilterCallback filterCallback) {
        this.filterCallback = filterCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false);
        /*getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN));*/
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setUpOnClickListeners();
        addChipsToChipGroup();

    }

    private void init() {
        listFilterDate = Arrays.asList(getResources().getStringArray(R.array.filter_arr));
 /*       dialogFragment = MonthYearPickerDialogFragment
                .getInstance(Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.YEAR));*/

        Bundle bundle = getArguments();
        if (bundle != null) {
            selectedDateRange = bundle.getString(BUNDLE_DATE_RANGE);
            stringChipCategory = bundle.getString(BUNDLE_CATEGORY);
            stringChipPayment = bundle.getString(BUNDLE_PAYMENT);
        }
        setUpFirstAndLastDayMonth();

        /*if (!selectedDateRange.isEmpty()) {
            if (selectedDateRange.contains(">")) {
                binding.spinnerDateRange.setSelection(0);
            } else {
                binding.spinnerDateRange.setSelection(1);
                String bundleMonth = selectedDateRange.substring(0, selectedDateRange.indexOf(","));
                String bundleYear = selectedDateRange.substring(selectedDateRange.indexOf(",") + 2);
                stringSelectedMonth  = bundleMonth;
                stringSelectedYear = bundleYear;
                List<String> arrMonth = Arrays.asList(getResources().getStringArray(R.array.month));
                int monthIndex = arrMonth.indexOf(bundleMonth);
                binding.etMonthYear.setText(selectedDateRange);

            }
        }*/
    }

    private void setUpFirstAndLastDayMonth() {
        Timber.d("selectedDateRange: " + selectedDateRange);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String currentDate = df.format(c.getTime());
        Timber.d("filter current date: %s", currentDate);

        String strFromDate = "", strToDate = "";
        if (selectedDateRange.contains(">")) {
            int separatorIndex = selectedDateRange.lastIndexOf(">");
            strFromDate = selectedDateRange.substring(0, separatorIndex - 1);
            strToDate = selectedDateRange.substring(separatorIndex + 2);
            Timber.d("fromdte: /" + strFromDate + "/");
            Timber.d("toDate: /" + strToDate + "/");
        } else {
            Timber.d("doesnt contain");
        }

        Date fromDate = Utils.convertToStoringDate(strFromDate, "23:59");
        Date toDate = Utils.convertToStoringDate(strToDate, "23:59");

        binding.etFromDate.setText(Utils.convertToDisplayDate(fromDate));
        binding.etToDate.setText(Utils.convertToDisplayDate(toDate));

        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTime(fromDate);
        mYearFrom = calendarFrom.get(Calendar.YEAR);
        mMonthFrom = calendarFrom.get(Calendar.MONTH);
        mDayFrom = calendarFrom.get(Calendar.DAY_OF_MONTH);

        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(toDate);
        mYearTo = calendarTo.get(Calendar.YEAR);
        mMonthTo = calendarTo.get(Calendar.MONTH);
        mDayTo = calendarTo.get(Calendar.DAY_OF_MONTH);

        stringFromDate = String.valueOf(binding.etFromDate.getText());
        stringToDate = String.valueOf(binding.etToDate.getText());

        String currentMonthYear = Utils.getMonthAndYear();
        binding.etMonthYear.setText(currentMonthYear);
        stringSelectedMonth = currentMonthYear.substring(0, currentMonthYear.indexOf(","));
        stringSelectedYear = currentMonthYear.substring(currentMonthYear.indexOf(",") + 2);
    }

    private void setUpOnClickListeners() {
        binding.ivClose.setOnClickListener(v -> dismiss());

        /*binding.spinnerDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringSpinnerDateFilter = listFilterDate.get(position);
                if (stringSpinnerDateFilter.equals("Date Range")) {
                    binding.linearLayoutDateRange.setVisibility(View.VISIBLE);
                    binding.linearLayoutMonthYear.setVisibility(View.GONE);
                } else {
                    binding.linearLayoutDateRange.setVisibility(View.GONE);
                    binding.linearLayoutMonthYear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/

        binding.etFromDate.setOnClickListener(v -> showDatePickerFrom());

        binding.etToDate.setOnClickListener(v -> showDatePickerTo());

        binding.etMonthYear.setOnClickListener(v -> {
            /*dialogFragment.show(requireActivity().getSupportFragmentManager(), null);*/

        });

/*        dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
            String[] monthArray = getResources().getStringArray(R.array.month);
            stringSelectedMonth = monthArray[monthOfYear];
            stringSelectedYear = String.valueOf(year);
            binding.etMonthYear.setText(stringSelectedMonth +", "+ stringSelectedYear);
        });*/
    }

    private void addChipsToChipGroup() {
        List<String> listExpCategory = Arrays.asList(getResources().getStringArray(R.array.category_arr_exp));
        List<String> listIncCategory = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.category_arr_inc)));
        listIncCategory.remove(0); //removing others category so no duplication

        List<String> totalCategoryList = new ArrayList<>();
        totalCategoryList.add("All Categories");
        totalCategoryList.addAll(listExpCategory);
        totalCategoryList.addAll(listIncCategory);

        for (int i = 0; i < totalCategoryList.size(); i++) {
            Chip chip = new Chip(requireContext());
            chip.setId(i);
            chip.setText(totalCategoryList.get(i));
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.chip_bg_color)));
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            if (totalCategoryList.get(i).equals(stringChipCategory)) chip.setChecked(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) stringChipCategory = chip.getText().toString();
            });
            binding.chipGroupCategories.addView(chip);
        }

        String[] arr_payment_mode = getResources().getStringArray(R.array.payment_mode);
        for (int i = 0; i < arr_payment_mode.length; i++) {
            Chip chip = new Chip(requireContext());
            chip.setId(i);
            chip.setText(arr_payment_mode[i]);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.chip_bg_color)));
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            if (arr_payment_mode[i].equals(stringChipPayment)) chip.setChecked(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) stringChipPayment = chip.getText().toString();
            });
            binding.chipGroupPaymentType.addView(chip);
        }
    }

    private void showDatePickerFrom() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, month, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    stringFromDate = format.format(calendar.getTime());

                    binding.etFromDate.setText(stringFromDate);
                    mYearFrom = year;
                    mMonthFrom = month;
                    mDayFrom = dayOfMonth;

                }, mYearFrom, mMonthFrom, mDayFrom);
        datePickerDialog.show();
    }

    private void showDatePickerTo() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, month, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    stringToDate = format.format(calendar.getTime());

                    binding.etToDate.setText(stringToDate);
                    mYearTo = year;
                    mMonthFrom = month;
                    mDayTo = dayOfMonth;

                }, mYearTo, mMonthTo, mDayTo);
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setUpResultCallBackData();
        binding = null;
    }

    private void setUpResultCallBackData() {
        String dateRange;
     /*   if (stringSpinnerDateFilter.equals("Select Date Range")) {
            dateRange = stringFromDate + " > " + stringToDate;
        } else {
            dateRange = stringSelectedMonth + ", " + stringSelectedYear;
        }*/
        dateRange = stringFromDate + " > " + stringToDate;
        filterCallback.filterResult(dateRange, stringChipCategory, stringChipPayment);

    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

}
