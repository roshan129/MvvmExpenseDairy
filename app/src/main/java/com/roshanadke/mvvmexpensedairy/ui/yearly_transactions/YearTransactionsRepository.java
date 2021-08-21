package com.roshanadke.mvvmexpensedairy.ui.yearly_transactions;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class YearTransactionsRepository {

    private ExpenseDao expenseDao;

    @Inject
    public YearTransactionsRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Flowable<List<ExpenseEntity>> getYearlyRecords(Date firstDay, Date lastDay) {
        return expenseDao.getYearWiseRecords(firstDay, lastDay);
    }

    public Flowable<Double> getYearlyExpense(Date firstDay, Date lastDay) {
        return expenseDao.getYearlyExpense(firstDay, lastDay);
    }

    public Flowable<Double> getYearlyIncome(Date firstDay, Date lastDay) {
        return expenseDao.getYearlyIncome(firstDay, lastDay);
    }


}
