package com.adivid.mvvmexpensedairy.ui.transactions_main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.adivid.mvvmexpensedairy.data.local.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;
import com.adivid.mvvmexpensedairy.domain.Expense;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class DashboardRepository {

    private ExpenseDao expenseDao;


    @Inject
    public DashboardRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public LiveData<List<ExpenseEntity>> getAllTransactions() {
        return expenseDao.getAllTransactions();
    }

    public Flowable<List<ExpenseEntity>> getAllMainTransactions() {
        return expenseDao.getAllMainTransactions();
    }
}
