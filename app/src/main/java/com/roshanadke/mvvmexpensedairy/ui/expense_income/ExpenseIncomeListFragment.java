package com.roshanadke.mvvmexpensedairy.ui.expense_income;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.EXPENSE_OR_INCOME;

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
import com.roshanadke.mvvmexpensedairy.databinding.FragmentExpenseIncomeListBinding;
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

@AndroidEntryPoint
public class ExpenseIncomeListFragment extends Fragment {

    private FragmentExpenseIncomeListBinding binding;
    private ExpenseIncomeViewModel viewModel;
    private CommonViewModel commonViewModel;
    private MainListAdapter adapter;
    private NavController navController;
    private List<ExpenseEntity> expenseEntityList;

    private Calendar c;
    private SimpleDateFormat df;
    private Date firstDay, lastDay;
    private String expense_income = "Expense";

    public ExpenseIncomeListFragment() {
        super(R.layout.fragment_expense_income_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentExpenseIncomeListBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();
        swipeOnRecyclerView(view);
    }

    private void init() {
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            expense_income =  bundle.getString(EXPENSE_OR_INCOME);
            binding.toolbar.setTitle(expense_income + " Report");
        }

        setUpRecyclerView();
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(ExpenseIncomeViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        expenseEntityList = new ArrayList<>();

    }

    private void observers() {
        viewModel.monthlyExpenseEntities.observe(getViewLifecycleOwner(), expenseEntities -> {
            expenseEntityList = expenseEntities;
            adapter.submitList(expenseEntities);
        });

        viewModel.monthlyExpense.observe(getViewLifecycleOwner(), s -> {
            if(expense_income.equalsIgnoreCase("Expense")){
                String exp = getString(R.string.rupee) + s;
                binding.tvMoneySpent.setText(exp);
            }else{
                binding.tvMoneySpent.setText(R.string._0_0);
            }
        });

        viewModel.monthlyIncome.observe(getViewLifecycleOwner(), s -> {
            if(expense_income.equalsIgnoreCase("Income")){
                String inc = getString(R.string.rupee) + s;
                binding.tvMoneyIncome.setText(inc);
            }else{
                binding.tvMoneyIncome.setText(R.string._0_0);
            }
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

    private void setUpOnClickListeners() {
        c = Calendar.getInstance();
        df = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        binding.buttonPrevious.setOnClickListener(v -> previousClicked());

        binding.buttonNext.setOnClickListener(v -> nextClicked());

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
                firstDay = getFirstDayOfMonth(s.toString());
                lastDay = getLastDayOfMonth(firstDay);
                viewModel.getMonthlyRecords(firstDay, lastDay, expense_income);
                getExpenseIncomeCount();
            }
        });

        binding.tvDate.setText(df.format(c.getTime()));

        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void getExpenseIncomeCount() {
        viewModel.getTotalMonthExpenseIncome(firstDay, lastDay, expense_income);
    }

    private void previousClicked() {
        c.add(Calendar.MONTH, -1);
        String formattedDate1 = df.format(c.getTime());
        Timber.d(formattedDate1);
        binding.tvDate.setText(formattedDate1);
    }

    private void nextClicked() {
        c.add(Calendar.MONTH, 1);
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
    private void swipeOnRecyclerView(View view) {
        binding.recyclerView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
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

    private Date getFirstDayOfMonth(String stringMonthYear) {
        stringMonthYear = "01 " + stringMonthYear + " 00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(stringMonthYear);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    private Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        return c.getTime();
    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(
                    R.id.action_expenseIncomeListFragment_to_addTransactionFragment, bundle);
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
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}
