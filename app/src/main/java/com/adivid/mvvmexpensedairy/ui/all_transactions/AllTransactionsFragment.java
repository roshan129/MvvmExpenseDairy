package com.adivid.mvvmexpensedairy.ui.all_transactions;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.databinding.FragmentAllTransactionsBinding;
import com.adivid.mvvmexpensedairy.domain.Expense;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class AllTransactionsFragment extends Fragment implements
        FragmentManager.OnBackStackChangedListener {

    private FragmentAllTransactionsBinding binding;
    private NavController navController;
    private AllTransactionsViewModel viewModel;
    private MainListAdapter adapter;
    private List<Expense> expenseList;

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
        Timber.d("inside all transactions");
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(AllTransactionsViewModel.class);

        expenseList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setAdapter(adapter);

    }

    private void observers() {
        viewModel.allTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            adapter.submitList(expenseEntities);
        });
    }

    private final OnItemClickListener recyclerViewClickListener =  new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Timber.d("list clicked" + position);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

    @Override
    public void onBackStackChanged() {
        navController.navigate(R.id.action_allTransactionsFragment2_to_dashboardFragment);
    }
}
