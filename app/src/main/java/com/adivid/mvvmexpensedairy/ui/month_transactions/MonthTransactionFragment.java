package com.adivid.mvvmexpensedairy.ui.month_transactions;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import timber.log.Timber;

@AndroidEntryPoint
public class MonthTransactionFragment extends Fragment {

    private FragmentMonthTransactionBinding binding;
    private MonthTransactionViewModel viewModel;
    private MainListAdapter adapter;
    private List<Expense> expenseList;

    public MonthTransactionFragment() {
        super(R.layout.fragment_month_transaction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMonthTransactionBinding.bind(view);

        init();
        observers();
    }

    private void init() {
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(MonthTransactionViewModel.class);
        expenseList = new ArrayList<>();

        viewModel.getMonthlyRecords("'2021'", "'02'");
    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observers() {
        viewModel.monthlyExpenseEntities.observe(getViewLifecycleOwner(), expenseEntities -> {
            expenseList.clear();
            for (int i = 0; i < expenseEntities.size(); i++) {
                Expense expense = new ExpenseEntityMapper().mapToDomainModel(expenseEntities.get(i));
                expenseList.add(expense);
            }
            adapter.submitList(expenseList);
        });
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
