package com.adivid.mvvmexpensedairy.ui.add_transactions;


import com.adivid.mvvmexpensedairy.data.local.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;

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

}
