package com.adivid.mvvmexpensedairy.ui.day_transactions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.databinding.FragmentDayTransactionsBinding;
import com.adivid.mvvmexpensedairy.domain.Expense;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        setUpOnClickListeners();
        observers();
    }

    private void init() {
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(DayTransactionViewModel.class);
        expenseList = new ArrayList<>();
        String today = Utils.getDisplayDate();
        binding.tvDate.setText(today);
        viewModel.getDayWiseRecords(today);
        viewModel.getTotalDayExpenseIncome(today);
    }

    private void setUpOnClickListeners() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

        binding.buttonPrevious.setOnClickListener(v -> {
            c.add(Calendar.DATE, -1);
            String formattedDate1 = df.format(c.getTime());
            Timber.d(formattedDate1);
            binding.tvDate.setText(formattedDate1);
        });

        binding.buttonNext.setOnClickListener(v -> {
            c.add(Calendar.DATE, 1);
            String formattedDate1 = df.format(c.getTime());

            Timber.d(formattedDate1);
            binding.tvDate.setText(formattedDate1);
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
                viewModel.getDayWiseRecords(s.toString());
                viewModel.getTotalDayExpenseIncome(s.toString());
                Timber.d("afterTextChanged" + s.toString());
            }
        });

        binding.ivClose.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observers() {
        viewModel.dayTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            adapter.submitList(expenseEntities);
        });

        viewModel.dayExpenseCount.observe(getViewLifecycleOwner(), s -> {
            String exp = getString(R.string.rupee) + s;
            binding.tvMoneySpent.setText(exp);
        });

        viewModel.dayIncomeCount.observe(getViewLifecycleOwner(), s -> {
            String inc = getString(R.string.rupee) + s;
            binding.tvMoneySpent.setText(inc);
        });
    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Timber.d("month list clicked" + position);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

}
