package com.adivid.mvvmexpensedairy.ui.day_transactions;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentDayTransactionsBinding;
import com.adivid.mvvmexpensedairy.ui.others.CommonViewModel;
import com.adivid.mvvmexpensedairy.utils.OnSwipeTouchListener;
import com.adivid.mvvmexpensedairy.utils.Resource;
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
    private CommonViewModel commonViewModel;
    private DayTransactionViewModel viewModel;
    private MainListAdapter adapter;
    private List<ExpenseEntity> expenseEntityList;
    private NavController navController;

    private Calendar c;
    private SimpleDateFormat df;
    private String selectedDate;

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
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
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
                selectedDate = s.toString();
                viewModel.getDayWiseRecords(selectedDate);
                getExpenseIncomeCount();
            }
        });

        binding.ivClose.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

    }

    private void getExpenseIncomeCount() {
        viewModel.getTotalDayExpenseIncome(selectedDate);
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

        commonViewModel.deleteRecord.observe(getViewLifecycleOwner(), integerResource -> {
            switch (integerResource.status) {
                case SUCCESS:
                    showProgressBar(false);
                    if (integerResource.data != null) {
                        showToast("Deleted Successfully");
                        commonViewModel.resetDeleteObserver();
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

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(
                    R.id.action_dayTransactionsFragment_to_addTransactionFragment, bundle);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            showAlertDialogToDelete(position);
        }
    };

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
        commonViewModel.deleteRecordFromDb(expenseEntityList.get(position));
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showProgressBar(boolean b) {
        if (b) binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.GONE);
    }

    private void resetTextViews() {
        binding.tvMoneySpent.setText(getString(R.string._0_0));
        binding.tvMoneyIncome.setText(getString(R.string._0_0));
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

}
