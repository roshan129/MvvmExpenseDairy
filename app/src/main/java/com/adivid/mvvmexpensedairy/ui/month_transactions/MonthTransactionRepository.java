package com.adivid.mvvmexpensedairy.ui.month_transactions;


import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class MonthTransactionRepository {

    private ExpenseDao expenseDao;

    @Inject
    public MonthTransactionRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;

    }

    public Flowable<List<ExpenseEntity>> getMonthlyRecords(Date firstDay, Date lastDay){
        return expenseDao.getMonthWiseRecords(firstDay, lastDay);
    }

    public Flowable<Double> getMonthlyExpense(Date firstDay, Date lastDay){
        return expenseDao.getTotalMonthExpense(firstDay, lastDay);
    }

    public Flowable<Double> getMonthlyIncome(Date firstDay, Date lastDay){
        return expenseDao.getTotalMonthIncome(firstDay, lastDay);
    }


}
