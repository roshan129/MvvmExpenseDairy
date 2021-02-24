package com.adivid.mvvmexpensedairy.ui.all_transactions;

import androidx.lifecycle.LiveData;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AllTransactionsRepository {

    private ExpenseDao expenseDao;
    private CompositeDisposable disposable;

    @Inject
    public AllTransactionsRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public LiveData<List<ExpenseEntity>> getAllTransactions() {
        return expenseDao.getAllTransactions();
    }

}
