package com.adivid.mvvmexpensedairy.ui.weekly_transactions;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.databinding.FragmentWeekTransactionsBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class WeekTransactionFragment extends Fragment {

    private FragmentWeekTransactionsBinding binding;
    private WeekTransactionViewModel viewModel;
    private Calendar calendarFirst, calendarLast;
    private MainListAdapter adapter;

    public WeekTransactionFragment() {
        super(R.layout.fragment_week_transactions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentWeekTransactionsBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();

    }

    private void init() {
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(WeekTransactionViewModel.class);

        binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(getFirstDayOfWeek()));
        binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(getLastDayOfWeek()));
        viewModel.getWeeklyReports(calendarFirst.getTime(), calendarLast.getTime());

    }

    private void observers() {
        viewModel.weeklyTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            adapter.submitList(expenseEntities);
        });
    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setUpOnClickListeners() {
        binding.buttonPrevious.setOnClickListener(v -> {
            Timber.d("clickes");
            calendarFirst.add(Calendar.DATE, -7);
            binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(calendarFirst.getTime()));

            calendarLast.add(Calendar.DATE, -7);
            binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(calendarLast.getTime()));
            viewModel.getWeeklyReports(calendarFirst.getTime(), calendarLast.getTime());
        });

        binding.buttonNext.setOnClickListener(v -> {
            Timber.d("clicked next");
            calendarFirst.add(Calendar.DATE, +7);
            binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(calendarFirst.getTime()));

            calendarLast.add(Calendar.DATE, +7);
            binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(calendarLast.getTime()));
            viewModel.getWeeklyReports(calendarFirst.getTime(), calendarLast.getTime());
        });

        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private Date getFirstDayOfWeek() {
        calendarFirst = Calendar.getInstance();
        calendarFirst.set(Calendar.DAY_OF_WEEK, 1);
        return calendarFirst.getTime();
    }

    private Date getLastDayOfWeek() {
        calendarLast = Calendar.getInstance();
        calendarLast.set(Calendar.DAY_OF_WEEK, 7);
        return calendarLast.getTime();
    }

    private OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };
}
