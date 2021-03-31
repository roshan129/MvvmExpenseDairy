package com.adivid.mvvmexpensedairy.ui.day_transactions;

import androidx.lifecycle.LiveData;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class DayTransactionRepository {

    private final ExpenseDao expenseDao;

    @Inject
    public DayTransactionRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;

    }

    public Flowable<List<ExpenseEntity>> getDayWiseRecords(Date daySt, Date dayEnd) {
        return expenseDao.getDayWiseRecords(daySt, dayEnd);
    }

    public Flowable<Double> getTotalDayExpense(Date dayStart, Date dayEnd) {
        return expenseDao.getTotalDayExpense(dayStart, dayEnd);
    }

    public Flowable<Double> getTotalDayIncome(Date dayStart, Date dayEnd) {
        return expenseDao.getTotalDayIncome(dayStart, dayEnd);
    }

}
