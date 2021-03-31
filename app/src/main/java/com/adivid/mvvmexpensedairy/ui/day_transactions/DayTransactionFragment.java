package com.adivid.mvvmexpensedairy.ui.day_transactions;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentDayTransactionsBinding;
import com.adivid.mvvmexpensedairy.utils.OnSwipeTouchListener;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;

@AndroidEntryPoint
public class DayTransactionFragment extends Fragment {

    private FragmentDayTransactionsBinding binding;
    private DayTransactionViewModel viewModel;
    private MainListAdapter adapter;
    private List<ExpenseEntity> expenseEntityList;
    private NavController navController;

    private Calendar c;
    private SimpleDateFormat df;

    public DayTransactionFragment() {
        super(R.layout.fragment_day_transactions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDayTransactionsBinding.bind(view);

        init();
        setUpOnClickListeners();
        observers();
        swipeForRecyclerView();
    }

    private void init() {
        setUpRecyclerView();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(DayTransactionViewModel.class);
        expenseEntityList = new ArrayList<>();
        String today = Utils.getDisplayDate();
        binding.tvDate.setText(today);
        viewModel.getDayWiseRecords(today);
        viewModel.getTotalDayExpenseIncome(today);
    }

    private void setUpOnClickListeners() {
        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        binding.buttonPrevious.setOnClickListener(v -> {
            previousClicked();
        });

        binding.buttonNext.setOnClickListener(v -> {
            nextClicked();
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
                resetTextViews();
                viewModel.getDayWiseRecords(s.toString());
                viewModel.getTotalDayExpenseIncome(s.toString());
                Timber.d("afterTextChanged" + s.toString());
            }
        });

        binding.ivClose.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

    }

    private void previousClicked() {
        c.add(Calendar.DATE, -1);
        String formattedDate1 = df.format(c.getTime());
        Timber.d(formattedDate1);
        binding.tvDate.setText(formattedDate1);
    }

    private void nextClicked() {
        c.add(Calendar.DATE, 1);
        String formattedDate1 = df.format(c.getTime());
        Timber.d(formattedDate1);
        binding.tvDate.setText(formattedDate1);
    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
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

    private void observers() {
        viewModel.dayTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            expenseEntityList = expenseEntities;
            adapter.submitList(expenseEntities);
        });

        viewModel.dayExpenseCount.observe(getViewLifecycleOwner(), s -> {
            String exp = getString(R.string.rupee) + s;
            binding.tvMoneySpent.setText(exp);
        });

        viewModel.dayIncomeCount.observe(getViewLifecycleOwner(), s -> {
            String inc = getString(R.string.rupee) + s;
            binding.tvMoneyIncome.setText(inc);
        });
    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(
                    R.id.action_dashboardFragment_to_addTransactionFragment, bundle);
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
