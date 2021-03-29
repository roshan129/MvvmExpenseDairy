package com.adivid.mvvmexpensedairy.ui.custom_view;

import android.os.Bundle;
import android.view.View;

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
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.util.ArrayList;
import java.util.List;

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

        expenseEntityList = new ArrayList<>();

        setUpRecyclerView();

        filterFromDate = "01 Mar, 2021";
        filterToDate = "31 Mar, 2021";
        filterCat = "";
        filterPay = "";

        binding.chipMonthYear.setText(filterFromDate + " > " + filterToDate);

        viewModel.getCustomList(Utils.convertToStoringDate(filterFromDate),
                Utils.convertToStoringDate(filterToDate), "", "");
        viewModel.getCustomExpenseIncomeCount(Utils.convertToStoringDate(filterFromDate),
                Utils.convertToStoringDate(filterToDate), "", "");

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
    }

    private void setUpOnClickListeners() {
        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.ivFilter.setOnClickListener(v -> loadFilterFragment());

        binding.chipMonthYear.setOnClickListener(v -> loadFilterFragment());

        binding.chipCategoryType.setOnClickListener(v -> loadFilterFragment());

        binding.chipPaymentMode.setOnClickListener(v -> loadFilterFragment());

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
        public void onLongItemClick(View view, int position) {

        }
    };

    private final FilterCallback filterResult = (dateRange, category, paymentType) -> {
        String fromDate = "", toDate = "";
        if (dateRange.contains(">")) {
            int separatorIndex = dateRange.lastIndexOf(">");
            fromDate = dateRange.substring(0, separatorIndex - 1);
            toDate = dateRange.substring(separatorIndex + 2);
            Timber.d("fromdte: %s", fromDate);
            Timber.d("toDate: %s", toDate);
        } else {
            Timber.d("doesnt contain");
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

        viewModel.getCustomList(Utils.convertToStoringDate(filterFromDate),
                Utils.convertToStoringDate(filterToDate), filterCat, filterPay);
        viewModel.getCustomExpenseIncomeCount(Utils.convertToStoringDate(filterFromDate),
                Utils.convertToStoringDate(filterToDate), filterCat, filterPay);

    };
}
