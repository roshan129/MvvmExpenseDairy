package com.adivid.mvvmexpensedairy.ui.add_transactions;


import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Maybe;

public class AddTransactionRepository {

    private ExpenseDao expenseDao;

    @Inject
    public AddTransactionRepository(ExpenseDao expenseDao) {
        this.expenseDao =expenseDao;
    }

    public Maybe<Long> insertTransaction(ExpenseEntity expenseEntity) {
        return expenseDao.insertTransaction(expenseEntity);
    }

    public Maybe<Integer> updateTransaction(ExpenseEntity expenseEntity) {
        return expenseDao.updateTransaction(expenseEntity);
    }
}
