package com.adivid.mvvmexpensedairy.ui.day_transactions;

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
import com.adivid.mvvmexpensedairy.databinding.FragmentDayTransactionsBinding;
import com.adivid.mvvmexpensedairy.domain.Expense;
import com.adivid.mvvmexpensedairy.domain.mapper.ExpenseEntityMapper;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class DayTransactionsFragment extends Fragment {

    private FragmentDayTransactionsBinding binding;
    private DayTransactionViewModel viewModel;
    private MainListAdapter adapter;
    private List<Expense> expenseList;

    public DayTransactionsFragment() {
        super(R.layout.fragment_day_transactions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDayTransactionsBinding.bind(view);

        init();
        observers();
    }

    private void init() {
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(DayTransactionViewModel.class);
        expenseList = new ArrayList<>();
    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observers() {
        viewModel.dayTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
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
            Timber.d("month list clicked" + position);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };
}
