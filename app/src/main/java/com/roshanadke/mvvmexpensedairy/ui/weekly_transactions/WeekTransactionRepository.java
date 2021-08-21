package com.roshanadke.mvvmexpensedairy.ui.weekly_transactions;


import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class WeekTransactionRepository {

    private ExpenseDao expenseDao;

    @Inject
    public WeekTransactionRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Flowable<List<ExpenseEntity>> getWeeklyReports(Date firstWeekDay, Date lastWeekDay) {
        return expenseDao.getWeeklyWiseRecords(firstWeekDay, lastWeekDay);
    }

    public Flowable<List<ExpenseEntity>> getWeeklyReportsOffset(Date firstWeekDay,
                                                                Date lastWeekDay,
                                                                int offset) {
        return expenseDao.getWeeklyWiseRecordsOffset(firstWeekDay, lastWeekDay, offset);
    }

    public Flowable<Double> getWeekExpense(Date firstWeekDay, Date lastWeekDay) {
        return expenseDao.getTotalWeekExpense(firstWeekDay, lastWeekDay);
    }

    public Flowable<Double> getWeekIncome(Date firstWeekDay, Date lastWeekDay) {
        return expenseDao.getTotalWeekIncome(firstWeekDay, lastWeekDay);
    }


}
