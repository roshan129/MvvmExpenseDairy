package com.adivid.mvvmexpensedairy.ui.day_transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DayTransactionViewModel extends ViewModel {

    private DayTransactionRepository repository;
    public LiveData<List<ExpenseEntity>> dayTransactions;

    @Inject
    public DayTransactionViewModel(DayTransactionRepository repository) {
        this.repository = repository;

        init();
    }

    private void init() {
        dayTransactions = repository.getDayWiseRecords();
    }
}
