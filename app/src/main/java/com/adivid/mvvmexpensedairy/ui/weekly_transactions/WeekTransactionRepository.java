package com.adivid.mvvmexpensedairy.ui.weekly_transactions;


import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import org.intellij.lang.annotations.Flow;
import org.w3c.dom.Entity;

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


}
