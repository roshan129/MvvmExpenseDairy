package com.adivid.mvvmexpensedairy.ui.weekly_transactions;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentWeekTransactionsBinding;
import com.adivid.mvvmexpensedairy.utils.OnSwipeTouchListener;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;

@AndroidEntryPoint
public class WeekTransactionFragment extends Fragment {

    private FragmentWeekTransactionsBinding binding;
    private WeekTransactionViewModel viewModel;
    private Calendar calendarFirst, calendarLast;
    private MainListAdapter adapter;

    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling, lockScrolling;
    private int currentItems, scrolledOutItems, totalItems;
    private int counter = 0;
    private List<ExpenseEntity> expenseEntityList;
    private Date weekFirstDate, weekLastDate;
    private NavController navController;

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
        swipeForRecyclerView();

    }

    private void init() {
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(WeekTransactionViewModel.class);
        expenseEntityList = new ArrayList<>();

        setUpRecyclerView();

        binding.tvWeekFirstDate.setText(Utils.convertToDisplayDate(getFirstDayOfWeek()));
        binding.tvWeekLastDate.setText(Utils.convertToDisplayDate(getLastDayOfWeek()));
        weekFirstDate = calendarFirst.getTime();
        weekLastDate = calendarLast.getTime();
        viewModel.getWeeklyReportsOffset(weekFirstDate, weekLastDate, counter);
        viewModel.getWeekExpenseIncome(weekFirstDate, weekLastDate);

    }

    private void observers() {
        viewModel.weeklyTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            if(!expenseEntities.isEmpty()) {
                expenseEntityList.addAll(expenseEntities);
                adapter.submitList(expenseEntityList);
                adapter.notifyDataSetChanged();
            }else {
                lockScrolling = true;
            }
        });

        viewModel.weeklyExpense.observe(getViewLifecycleOwner(), s -> {
            String exp = getString(R.string.rupee) + s;
            binding.tvMoneySpent.setText(exp);
        });

        viewModel.weeklyIncome.observe(getViewLifecycleOwner(), s -> {
            String inc = getString(R.string.rupee) + s;
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

                if (isScrolling && (currentItems + scrolledOutItems == totalItems) && !lockScrolling) {
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
            previousClicked();
        });

        binding.buttonNext.setOnClickListener(v -> {
            nextClicked();
        });

        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void swipeForRecyclerView() {
        binding.recyclerView.setOnTouchListener(new OnSwipeTouchListener(requireContext()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                previousClicked();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextClicked();
            }
        });
    }

    private void previousClicked() {
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
    }

    private void nextClicked() {
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
    }

    private Date getFirstDayOfWeek() {
        calendarFirst = Calendar.getInstance();
        calendarFirst.set(Calendar.DAY_OF_WEEK, 1);
        calendarFirst.set(Calendar.HOUR, 0);
        calendarFirst.set(Calendar.MINUTE, 0);
        calendarFirst.set(Calendar.SECOND, 0);
        calendarFirst.set(Calendar.HOUR_OF_DAY, 0);
        return calendarFirst.getTime();
    }

    private Date getLastDayOfWeek() {
        calendarLast = Calendar.getInstance();
        calendarLast.set(Calendar.DAY_OF_WEEK, 7);
        calendarLast.set(Calendar.HOUR, 23);
        calendarLast.set(Calendar.MINUTE, 59);
        calendarLast.set(Calendar.SECOND, 59);
        calendarLast.set(Calendar.HOUR_OF_DAY, 23);
        return calendarLast.getTime();
    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(
                    R.id.action_weekTransactionFragment_to_addTransactionFragment, bundle);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    };

    private void resetTextViews() {
        binding.tvMoneySpent.setText(getString(R.string._000_0));
        binding.tvMoneyIncome.setText(getString(R.string._000_0));
        lockScrolling = false;
    }
}
