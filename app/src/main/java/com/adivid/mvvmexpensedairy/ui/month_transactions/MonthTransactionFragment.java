package com.adivid.mvvmexpensedairy.ui.month_transactions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.databinding.FragmentMonthTransactionBinding;
import com.adivid.mvvmexpensedairy.domain.Expense;
import com.adivid.mvvmexpensedairy.domain.mapper.ExpenseEntityMapper;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

import timber.log.Timber;

@AndroidEntryPoint
public class MonthTransactionFragment extends Fragment {

    private FragmentMonthTransactionBinding binding;
    private MonthTransactionViewModel viewModel;
    private MainListAdapter adapter;

    public MonthTransactionFragment() {
        super(R.layout.fragment_month_transaction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMonthTransactionBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        binding.buttonPrevious.setOnClickListener(v -> {
            c.add(Calendar.MONTH, -1);
            String formattedDate1 = df.format(c.getTime());
            Timber.d(formattedDate1);
            binding.tvDate.setText(formattedDate1);
        });

        binding.buttonNext.setOnClickListener(v -> {
            c.add(Calendar.MONTH, 1);
            String formattedDate1 = df.format(c.getTime());

            Timber.d(formattedDate1);
            binding.tvDate.setText(formattedDate1);
        });

        binding.tvDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Date firstDay = getFirstDayOfMonth(s.toString());
                Date lastDay = getLastDayOfMonth(firstDay);
                viewModel.getMonthlyRecords(firstDay, lastDay);
            }
        });

        binding.tvDate.setText(df.format(c.getTime()));

        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void init() {
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(MonthTransactionViewModel.class);

    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

    }

    private void observers() {
        viewModel.monthlyExpenseEntities.observe(getViewLifecycleOwner(), expenseEntities -> {
            adapter.submitList(expenseEntities);
        });
    }

    private Date getFirstDayOfMonth(String stringMonthYear) {
        stringMonthYear = "01 " + stringMonthYear;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(stringMonthYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    private Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    private final OnItemClickListener recyclerViewClickListener =  new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Timber.d("day list clicked" + position);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

}
