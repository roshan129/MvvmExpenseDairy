package com.adivid.mvvmexpensedairy.ui.transactions_main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.MainActivity;
import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentDashboardBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private NavController navController;
    private DashboardViewModel viewModel;
    private List<ExpenseEntity> expenseEntityList;
    private MainListAdapter adapter;

    private boolean doubleBackToExitPressedOnce = false;

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashboardBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();

    }

    private void init() {
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                onBackPressedCallback
        );
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        expenseEntityList = new ArrayList<>();

        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

    }

    private void observers() {
        viewModel.recentAllTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            this.expenseEntityList = expenseEntities;
            adapter.submitList(expenseEntities);
            adapter.notifyDataSetChanged();
        });

        viewModel.expenseCount.observe(getViewLifecycleOwner(), aDouble -> {
            if(aDouble == null) binding.tvExpMoney.setText("0");
            else binding.tvExpMoney.setText(String.valueOf(aDouble));
        });

        viewModel.incomeCount.observe(getViewLifecycleOwner(), aDouble -> {
            if(aDouble == null) binding.tvIncomeMoney.setText("0");
            else binding.tvIncomeMoney.setText(String.valueOf(aDouble));
        });

    }

    private void setUpOnClickListeners() {
        binding.fabAdd.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_addTransactionFragment);
        });

        binding.ivMenu.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openDrawer();
        });

        binding.cardWeekly.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_weekTransactionFragment);
        });

        binding.cardMonthly.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_monthTransactionFragment);
        });

        binding.cardYearly.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_yearTransactionFragment);
        });

        binding.tvSeeAll.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_allTransactionsFragment);
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

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish();
            }
            doubleBackToExitPressedOnce = true;
            Snackbar.make(requireActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    "Press Back Again to Exit", Snackbar.LENGTH_SHORT).show();
            long DOUBLE_BACK_PRESS_TO_EXIT = 2000;
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    doubleBackToExitPressedOnce = false, DOUBLE_BACK_PRESS_TO_EXIT);
        }
    };

}
