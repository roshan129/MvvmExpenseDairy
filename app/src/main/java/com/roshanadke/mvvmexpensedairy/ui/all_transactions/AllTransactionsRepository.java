package com.roshanadke.mvvmexpensedairy.ui.all_transactions;

import androidx.lifecycle.LiveData;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AllTransactionsRepository {

    private final ExpenseDao expenseDao;
    private CompositeDisposable disposable;

    @Inject
    public AllTransactionsRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public LiveData<List<ExpenseEntity>> getAllTransactions() {
        return expenseDao.getAllTransactions();
    }

    public Observable<List<ExpenseEntity>> getAllTransactionsOffset(int offset) {
        return expenseDao.getAllMainTransactionsOffset(offset);
    }
}
