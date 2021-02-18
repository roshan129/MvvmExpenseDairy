package com.adivid.mvvmexpensedairy.ui.day_transactions;

import androidx.lifecycle.LiveData;

import com.adivid.mvvmexpensedairy.data.local.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

public class DayTransactionRepository {

    private ExpenseDao expenseDao;

    @Inject
    public DayTransactionRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;

    }

    public LiveData<List<ExpenseEntity>> getDayWiseRecords() {
        return expenseDao.getDayWiseRecords();
    }

}
