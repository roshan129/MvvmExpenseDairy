package com.adivid.mvvmexpensedairy.ui.all_transactions;

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
import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentAllTransactionsBinding;
import com.adivid.mvvmexpensedairy.domain.Expense;
import com.adivid.mvvmexpensedairy.domain.mapper.ExpenseEntityMapper;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AllTransactionsFragment extends Fragment {

    private FragmentAllTransactionsBinding binding;
    private AllTransactionsViewModel viewModel;
    private MainListAdapter adapter;

    public AllTransactionsFragment() {
        super(R.layout.fragment_all_transactions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAllTransactionsBinding.bind(view);

        init();
        observers();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(AllTransactionsViewModel.class);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setAdapter(adapter);

    }

    private void observers() {
        viewModel.allTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            List<Expense> list = new ArrayList<>();
            for (int i = 0; i < expenseEntities.size(); i++) {
                Expense expense = new ExpenseEntityMapper().mapToDomainModel(expenseEntities.get(i));
                list.add(expense);
            }
            adapter.submitList(list);
        });
    }

    private OnItemClickListener recyclerViewClickListener =  new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Timber.d("list clicked" + position);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

}
