package com.adivid.mvvmexpensedairy.ui.dashboard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.adivid.mvvmexpensedairy.MainActivity;
import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentDashboardBinding;
import com.adivid.mvvmexpensedairy.utils.DataSyncWorker;
import com.adivid.mvvmexpensedairy.utils.DeleteDataWorker;
import com.adivid.mvvmexpensedairy.utils.Resource;
import com.adivid.mvvmexpensedairy.utils.SharedPrefManager;
import com.adivid.mvvmexpensedairy.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;
import static com.adivid.mvvmexpensedairy.utils.Constants.KEY_DELETE_UNIQUE_WORK;
import static com.adivid.mvvmexpensedairy.utils.Constants.KEY_UNIQUE_WORK;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private NavController navController;
    private DashboardViewModel viewModel;
    private List<ExpenseEntity> expenseEntityList;
    private MainListAdapter adapter;

    @Inject
    public SharedPrefManager sharedPrefManager;

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
        syncOfflineDataToServer();
    }

    private void init() {
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                onBackPressedCallback
        );
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        expenseEntityList = new ArrayList<>();

        if (!sharedPrefManager.getUserName().isEmpty())
            binding.tvUsername.setText(sharedPrefManager.getUserName());

        Timber.d("username: %s", sharedPrefManager.getUserName());
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
            String exp;
            if (aDouble == null) {
                exp = getString(R.string.rupee) + "0";
                binding.tvExpMoney.setText(exp);
            } else {
                exp = getString(R.string.rupee) + aDouble;
                binding.tvExpMoney.setText(exp);
            }
            calculateBalance();
        });

        viewModel.incomeCount.observe(getViewLifecycleOwner(), aDouble -> {
            String inc;
            if (aDouble == null) {
                inc = getString(R.string.rupee) + "0";
                binding.tvIncomeMoney.setText(inc);
            } else {
                inc = getString(R.string.rupee) + aDouble;
                binding.tvIncomeMoney.setText(inc);
            }
            calculateBalance();
        });

        viewModel.deleteRecord.observe(getViewLifecycleOwner(), integerResource -> {
            switch (integerResource.status) {
                case SUCCESS:
                    showProgressBar(false);
                    if (integerResource.data != null) {
                        showToast("Deleted Successfully");
                        viewModel.resetDeleteObserver();
                        Utils.syncDeletedRecords(requireContext());
                    }
                    break;
                case LOADING:
                    showProgressBar(true);
                    break;
                case ERROR:
                    showProgressBar(false);
                    showToast("Some Error Occurred");
                    break;
            }
        });

    }

    private void calculateBalance() {
        String exp = binding.tvExpMoney.getText().toString();
        String inc = binding.tvIncomeMoney.getText().toString();

        String expSubStr = exp.substring(exp.indexOf(getString(R.string.rupee)) + 1);
        String incSubStr = inc.substring(inc.indexOf(getString(R.string.rupee)) + 1);

        double bal = Double.parseDouble(incSubStr) - Double.parseDouble(expSubStr);
        String balance = getString(R.string.rupee) + bal;
        binding.tvBalanceMoney.setText(balance);

    }

    private void setUpOnClickListeners() {
        binding.fabAdd.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_addTransactionFragment);
        });

        binding.ivMenu.setOnClickListener(v -> ((MainActivity) requireActivity()).openDrawer());

        binding.cardWeekly.setOnClickListener(v ->
                navController.navigate(R.id.action_dashboardFragment_to_weekTransactionFragment));

        binding.cardMonthly.setOnClickListener(v ->
                navController.navigate(R.id.action_dashboardFragment_to_monthTransactionFragment));

        binding.cardYearly.setOnClickListener(v ->
                navController.navigate(R.id.action_dashboardFragment_to_yearTransactionFragment));

        binding.tvSeeAll.setOnClickListener(v ->
                navController.navigate(R.id.action_dashboardFragment_to_allTransactionsFragment));

        binding.profileImage.setOnClickListener(v ->
                navController.navigate(R.id.action_dashboardFragment_to_signInFragment));
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
        public void onItemLongClick(View view, int position) {
            showAlertDialogToDelete(position);
        }
    };

    private void syncOfflineDataToServer() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DataSyncWorker.class)
                .setConstraints(constraints)
                .addTag(KEY_UNIQUE_WORK)
                .build();
        WorkManager.getInstance(requireContext()).enqueueUniqueWork(KEY_UNIQUE_WORK,
                ExistingWorkPolicy.KEEP, request);
    }


    private void showAlertDialogToDelete(int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteRecordFromDb(position);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .create().show();
    }

    private void deleteRecordFromDb(int position) {
        viewModel.deleteRecordFromDb(expenseEntityList.get(position));
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showProgressBar(boolean b) {
        if (b) binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.GONE);
    }

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
                    "Press Back Again to Exit", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getResources().getColor(R.color.color_on_dark_gray_2))
                    .setTextColor(getResources().getColor(R.color.white))
                    .show();
            long DOUBLE_BACK_PRESS_TO_EXIT = 2000;
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                    doubleBackToExitPressedOnce = false, DOUBLE_BACK_PRESS_TO_EXIT);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setUpDrawerLayoutHeaders();
    }
}
