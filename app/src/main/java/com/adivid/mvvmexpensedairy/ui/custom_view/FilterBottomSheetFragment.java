package com.adivid.mvvmexpensedairy.ui.custom_view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.databinding.FragmentFilterBottomSheetBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    private FragmentFilterBottomSheetBinding binding;

    private List<String> listFilterDate;
    private String selectedDateFilter, stringFromDate, stringToDate;
    private int mYearFrom, mMonthFrom, mDayFrom, mYearTo, mMonthTo, mDayTo;

    public FilterBottomSheetFragment() {
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

    }

    private void init() {
        listFilterDate = Arrays.asList(getResources().getStringArray(R.array.filter_arr));

        Calendar c = Calendar.getInstance();
        mYearFrom = c.get(Calendar.YEAR);
        mMonthFrom = c.get(Calendar.MONTH);
        mDayFrom = c.get(Calendar.DAY_OF_MONTH);

        mYearTo = c.get(Calendar.YEAR);
        mMonthTo = c.get(Calendar.MONTH);
        mDayTo = c.get(Calendar.DAY_OF_MONTH);

        setUpFirstAndLastDayMonth();

    }

    private void setUpFirstAndLastDayMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String currentDate = df.format(c.getTime());
        Timber.d("filter current date: " + currentDate);

        Date fromDate = Utils.getFirstDayOfMonth(currentDate);
        Date toDate = Utils.getLastDayOfMonth(fromDate);

        Timber.d("Date from: " + fromDate.toString());
        Timber.d("Date to: " + toDate.toString());

        binding.etFromDate.setText(Utils.convertToDisplayDate(fromDate));
        binding.etToDate.setText(Utils.convertToDisplayDate(toDate));

    }

    private void setUpOnClickListeners() {
        binding.ivClose.setOnClickListener(v -> {
            dismiss();
        });

        binding.spinnerDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDateFilter = listFilterDate.get(position);
                if(selectedDateFilter.equals("Date Range")){
                    binding.linearLayoutDateRange.setVisibility(View.VISIBLE);
                }else{
                    //binding.linearLayoutDateRange.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.etFromDate.setOnClickListener(v -> {
            showDatePickerFrom();
        });

        binding.etToDate.setOnClickListener(v -> {
            showDatePickerTo();
        });
    }

    private void showDatePickerFrom() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {
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
                (DatePickerDialog.OnDateSetListener) (view, year, month, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    stringToDate = format.format(calendar.getTime());

                    binding.etToDate.setText(stringToDate);
                    mYearFrom = year;
                    mMonthFrom = month;
                    mDayFrom = dayOfMonth;

                }, mYearFrom, mMonthFrom, mDayFrom);
        datePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

}
