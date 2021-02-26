package com.adivid.mvvmexpensedairy.ui.yearly_transactions;

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
import com.adivid.mvvmexpensedairy.databinding.FragmentYearTransactionBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class YearTransactionFragment extends Fragment {

    private FragmentYearTransactionBinding binding;
    private YearTransactionViewModel viewModel;
    private MainListAdapter adapter;

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
    }

    private void init() {
        setUpRecyclerView();
        viewModel = new ViewModelProvider(this).get(YearTransactionViewModel.class);
        viewModel.getYearlyRecords(Utils.convertToStoringDate("01 Jan, 2021"),
                Utils.convertToStoringDate("31 Dec, 2021"));
    }

    private void setUpOnClickListeners() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());

        binding.buttonPrevious.setOnClickListener(v -> {
            c.add(Calendar.YEAR, -1);
            String formattedDate1 = df.format(c.getTime());
            Timber.d(formattedDate1);
            binding.tvDate.setText(formattedDate1);
        });

        binding.buttonNext.setOnClickListener(v -> {
            c.add(Calendar.YEAR, 1);
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
                Date firstDay = getFirstDayOfYear(s.toString());
                Date lastDay = getLastDayOfYear(s.toString());
                viewModel.getYearlyRecords(firstDay, lastDay);
            }
        });

        binding.tvDate.setText(df.format(c.getTime()));

        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    private void observers() {
        viewModel.yearlyExpenseTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            adapter.submitList(expenseEntities);
        });
    }

    private void setUpRecyclerView() {
        adapter = new MainListAdapter(recyclerItemClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private Date getFirstDayOfYear(String year) {
        String firstDay = "01 " + "Jan, " + year;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        Date newDate = null;
        try {
            newDate = sdf.parse(firstDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    private Date getLastDayOfYear(String year) {
        String lastDay = "31 " + "Dec, " + year;
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
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

        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

}
