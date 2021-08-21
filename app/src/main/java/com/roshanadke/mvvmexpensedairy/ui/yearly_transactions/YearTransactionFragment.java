package com.roshanadke.mvvmexpensedairy.ui.yearly_transactions;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.roshanadke.mvvmexpensedairy.R;
import com.roshanadke.mvvmexpensedairy.adapter.MainListAdapter;
import com.roshanadke.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.databinding.FragmentYearTransactionBinding;
import com.roshanadke.mvvmexpensedairy.ui.others.CommonViewModel;
import com.roshanadke.mvvmexpensedairy.utils.OnSwipeTouchListener;
import com.roshanadke.mvvmexpensedairy.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;

@AndroidEntryPoint
public class YearTransactionFragment extends Fragment {

    private FragmentYearTransactionBinding binding;
    private YearTransactionViewModel viewModel;
    private CommonViewModel commonViewModel;
    private MainListAdapter adapter;
    private NavController navController;
    private List<ExpenseEntity> expenseEntityList;

    private Calendar c;
    private SimpleDateFormat df;
    private Date firstDay, lastDay;

    public YearTransactionFragment() {
        super(R.layout.fragment_year_transaction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentYearTransactionBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();
        swipeForRecyclerView();
    }

    private void init() {
        navController = NavHostFragment.findNavController(this);
        setUpRecyclerView();
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        viewModel = new ViewModelProvider(this).get(YearTransactionViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        expenseEntityList = new ArrayList<>();
    }

    private void setUpOnClickListeners() {
        c = Calendar.getInstance();
        df = new SimpleDateFormat("yyyy", Locale.getDefault());

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
                firstDay = getFirstDayOfYear(s.toString());
                lastDay = getLastDayOfYear(s.toString());
                viewModel.getYearlyRecords(firstDay, lastDay);
                getExpenseIncomeCount();
            }
        });

        binding.tvDate.setText(df.format(c.getTime()));

        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void getExpenseIncomeCount() {
        viewModel.getYearlyExpenseIncome(firstDay, lastDay);
    }

    private void observers() {
        viewModel.yearlyExpenseTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            expenseEntityList = expenseEntities;
            adapter.submitList(expenseEntities);
        });

        viewModel.yearlyExpense.observe(getViewLifecycleOwner(), s -> {
            String exp = getString(R.string.rupee) + s;
            binding.tvMoneySpent.setText(exp);
        });

        viewModel.yearlyIncome.observe(getViewLifecycleOwner(), s -> {
            String inc = getString(R.string.rupee) + s;
            binding.tvMoneyIncome.setText(inc);
        });

        commonViewModel.deleteRecord.observe(getViewLifecycleOwner(), integerResource -> {
            switch (integerResource.status) {
                case SUCCESS:
                    showProgressBar(false);
                    if (integerResource.data != null) {
                        getExpenseIncomeCount();
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

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerItemClickListener);
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

    private void previousClicked() {
        c.add(Calendar.YEAR, -1);
        String formattedDate1 = df.format(c.getTime());
        Timber.d(formattedDate1);
        binding.tvDate.setText(formattedDate1);
    }

    private void nextClicked() {
        c.add(Calendar.YEAR, 1);
        String formattedDate1 = df.format(c.getTime());
        Timber.d(formattedDate1);
        binding.tvDate.setText(formattedDate1);
    }

    private Date getFirstDayOfYear(String year) {
        String firstDay = "01 " + "Jan, " + year + " 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(firstDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    private Date getLastDayOfYear(String year) {
        String lastDay = "31 " + "Dec, " + year + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy HH:mm:ss", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    private final OnItemClickListener recyclerItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(
                    R.id.action_yearTransactionFragment_to_addTransactionFragment, bundle);
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
