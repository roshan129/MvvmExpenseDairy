package com.adivid.mvvmexpensedairy.ui.custom_view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.FilterCallback;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentCustomViewBinding;
import com.adivid.mvvmexpensedairy.ui.others.CommonViewModel;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.BUNDLE_CATEGORY;
import static com.adivid.mvvmexpensedairy.utils.Constants.BUNDLE_DATE_RANGE;
import static com.adivid.mvvmexpensedairy.utils.Constants.BUNDLE_PAYMENT;
import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;

@AndroidEntryPoint
public class CustomViewFragment extends Fragment {

    private FragmentCustomViewBinding binding;
    private CustomViewViewModel viewModel;
    private CommonViewModel commonViewModel;
    private List<ExpenseEntity> expenseEntityList;
    private MainListAdapter adapter;
    private NavController navController;

    private String filterFromDate, filterToDate, filterCat, filterPay;

    public CustomViewFragment() {
        super(R.layout.fragment_custom_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCustomViewBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();
    }

    private void init() {
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(CustomViewViewModel.class);
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);

        expenseEntityList = new ArrayList<>();

        setUpRecyclerView();

        setUpFirstAndLastDayMonth();
        //filterFromDate = "01 Mar, 2021";
        //filterToDate = "31 Mar, 2021";
        filterCat = "";
        filterPay = "";

        binding.chipMonthYear.setText(filterFromDate + " > " + filterToDate);

        viewModel.getCustomList(Utils.convertToStoringDate(filterFromDate, "00:00"),
                Utils.convertToStoringDate(filterToDate, "23:59"), "", "");
        viewModel.getCustomExpenseIncomeCount(Utils.convertToStoringDate(filterFromDate, "00:00"),
                Utils.convertToStoringDate(filterToDate, "23:59"), "", "");

    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observers() {
        viewModel.customTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            expenseEntityList = expenseEntities;
            adapter.submitList(expenseEntityList);
        });

        viewModel.customExpenseCount.observe(getViewLifecycleOwner(), s -> {
            String exp = getString(R.string.rupee) + s;
            binding.tvMoneySpent.setText(exp);
        });

        viewModel.customIncomeCount.observe(getViewLifecycleOwner(), s -> {
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

    private void setUpOnClickListeners() {
        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.ivFilter.setOnClickListener(v -> loadFilterFragment());

        binding.chipMonthYear.setOnClickListener(v -> loadFilterFragment());

        binding.chipCategoryType.setOnClickListener(v -> loadFilterFragment());

        binding.chipPaymentMode.setOnClickListener(v -> loadFilterFragment());

    }

    private void setUpFirstAndLastDayMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String currentDate = df.format(c.getTime());
        Timber.d("filter current date: %s", currentDate);

        Date fromDate = Utils.getFirstDayOfMonth(currentDate);
        Date toDate = Utils.getLastDayOfMonth(fromDate);

        filterFromDate = Utils.convertToDisplayDate(fromDate);
        filterToDate = Utils.convertToDisplayDate(toDate);

    }

    private void loadFilterFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_DATE_RANGE, String.valueOf(binding.chipMonthYear.getText()));
        bundle.putString(BUNDLE_CATEGORY, String.valueOf(binding.chipCategoryType.getText()));
        bundle.putString(BUNDLE_PAYMENT, String.valueOf(binding.chipPaymentMode.getText()));

        FilterBottomSheetFragment fragment = new FilterBottomSheetFragment(filterResult);
        fragment.setArguments(bundle);
        fragment.show(getChildFragmentManager(), "FilterBottomSheetFragment");
        fragment.setCancelable(true);
    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(
                    R.id.action_customViewFragment_to_addTransactionFragment, bundle);
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

    private final FilterCallback filterResult = (dateRange, category, paymentType) -> {
        String fromDate = "", toDate = "";
        Date dateFrom = null, toFrom = null;
        if (dateRange.contains(">")) {
            int separatorIndex = dateRange.lastIndexOf(">");
            fromDate = dateRange.substring(0, separatorIndex - 1);
            toDate = dateRange.substring(separatorIndex + 2);
            Timber.d("fromdte: %s", fromDate);
            Timber.d("toDate: %s", toDate);
        } else {
            dateFrom = Utils.getFirstDayOfMonthFromMonthYear(dateRange);
            toFrom = Utils.getLastDayOfMonth(dateFrom);

        }
        binding.chipMonthYear.setText(dateRange);
        binding.chipCategoryType.setText(category);
        binding.chipPaymentMode.setText(paymentType);

        if (category.equals(getString(R.string.all_categories))) category = "";
        if (paymentType.equals("All Payment Modes")) paymentType = "";

        filterFromDate = fromDate;
        filterToDate = toDate;
        filterCat = category;
        filterPay = paymentType;

        Timber.d("filter data: " + filterFromDate + filterToDate + filterCat + filterPay);

        if (dateRange.contains(">")) {
            viewModel.getCustomList(Utils.convertToStoringDate(filterFromDate, "00:00"),
                    Utils.convertToStoringDate(filterToDate, "23:59"), filterCat, filterPay);
            getExpenseIncomeCount();
        }/*else{
            viewModel.getCustomList(dateFrom,
                    toFrom, filterCat, filterPay);
            viewModel.getCustomExpenseIncomeCount(dateFrom,
                    toFrom, filterCat, filterPay);
        }*/

    };

    private void getExpenseIncomeCount() {
        viewModel.getCustomExpenseIncomeCount(Utils.convertToStoringDate(filterFromDate , "00:00"),
                Utils.convertToStoringDate(filterToDate, "23:59"), filterCat, filterPay);
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

}
