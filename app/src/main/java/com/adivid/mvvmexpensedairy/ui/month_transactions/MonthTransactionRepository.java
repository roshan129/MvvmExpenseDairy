package com.adivid.mvvmexpensedairy.ui.month_transactions;


import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class MonthTransactionRepository {

    private ExpenseDao expenseDao;

    @Inject
    public MonthTransactionRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;

    }

    public Flowable<List<ExpenseEntity>> getMonthlyRecords(String month, String year){
        return expenseDao.getMonthWiseRecords(month, year);
    }

}
