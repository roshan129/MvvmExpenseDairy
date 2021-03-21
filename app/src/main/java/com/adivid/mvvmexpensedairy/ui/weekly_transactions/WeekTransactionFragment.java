package com.adivid.mvvmexpensedairy.ui.weekly_transactions;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentWeekTransactionsBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class WeekTransactionFragment extends Fragment {

    private FragmentWeekTransactionsBinding binding;
    private WeekTransactionViewModel viewModel;
    private Calendar calendarFirst, calendarLast;
    private MainListAdapter adapter;

    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling;
    private int currentItems, scrolledOutItems, totalItems;
    private int counter = 0;
    private List<ExpenseEntity> expenseEntityList;
    private Date weekFirstDate, weekLastDate;

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
        viewModel = new ViewModelProvider(this).get(WeekTransactionViewModel.class);
        expenseEntityList = new ArrayList<>();

        setUpRecyclerView();

        binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(getFirstDayOfWeek()));
        binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(getLastDayOfWeek()));
        weekFirstDate = calendarFirst.getTime();
        Timber.d("weekFirstDate"   + weekFirstDate.toString());
        Timber.d("weekFirstDate gettime"   + weekFirstDate.getTime());
        weekLastDate = calendarLast.getTime();
        viewModel.getWeeklyReportsOffset(weekFirstDate, weekLastDate, counter);
        viewModel.getWeekExpenseIncome(weekFirstDate, weekLastDate);

    }

    private void observers() {
        viewModel.weeklyTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            expenseEntityList.addAll(expenseEntities);
            adapter.submitList(expenseEntityList);
            adapter.notifyDataSetChanged();
        });

        viewModel.weeklyExpense.observe(getViewLifecycleOwner(), s -> {
            String exp = getString(R.string.rupee)  + s;
            binding.tvMoneySpent.setText(exp);
        });

        viewModel.weeklyIncome.observe(getViewLifecycleOwner(), s -> {
            String inc = getString(R.string.rupee)  + s;
            binding.tvMoneyIncome.setText(inc);
        });
    }

    private void setUpRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(requireContext());
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                    //binding.pro.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    counter++;
                    String offset = counter + "0";
                    viewModel.getWeeklyReportsOffset(weekFirstDate, weekLastDate,
                            Integer.parseInt(offset));
                }
            }
        });
    }

    private void setUpOnClickListeners() {
        binding.buttonPrevious.setOnClickListener(v -> {
            resetTextViews();
            counter = 0;

            expenseEntityList.clear();
            adapter.submitList(expenseEntityList);
            calendarFirst.add(Calendar.DATE, -7);
            binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(calendarFirst.getTime()));

            calendarLast.add(Calendar.DATE, -7);
            binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(calendarLast.getTime()));
            weekFirstDate = calendarFirst.getTime();
            weekLastDate = calendarLast.getTime();
            viewModel.getWeeklyReportsOffset(weekFirstDate, weekLastDate, counter);
            viewModel.getWeekExpenseIncome(weekFirstDate, weekLastDate);


        });

        binding.buttonNext.setOnClickListener(v -> {
            resetTextViews();
            counter = 0;
            expenseEntityList.clear();
            adapter.submitList(expenseEntityList);
            calendarFirst.add(Calendar.DATE, +7);
            binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(calendarFirst.getTime()));

            calendarLast.add(Calendar.DATE, +7);
            binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(calendarLast.getTime()));
            weekFirstDate = calendarFirst.getTime();
            weekLastDate = calendarLast.getTime();
            viewModel.getWeeklyReportsOffset(weekFirstDate, weekLastDate, counter);
            viewModel.getWeekExpenseIncome(weekFirstDate, weekLastDate);

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

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

    private void resetTextViews() {
        binding.tvMoneySpent.setText(getString(R.string._000_0));
        binding.tvMoneyIncome.setText(getString(R.string._000_0));
    }
}
