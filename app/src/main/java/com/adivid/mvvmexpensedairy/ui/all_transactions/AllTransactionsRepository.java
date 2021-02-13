package com.adivid.mvvmexpensedairy.ui.all_transactions;

import com.adivid.mvvmexpensedairy.data.local.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AllTransactionsRepository {

    private ExpenseDao expenseDao;
    private CompositeDisposable disposable;

    @Inject
    public AllTransactionsRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Flowable<List<ExpenseEntity>> getAllTransactions() {
        return expenseDao.getAllTransactions();
    }

}
