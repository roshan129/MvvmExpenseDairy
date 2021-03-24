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

    public Flowable<List<ExpenseEntity>> getDayWiseRecords(Date daySt) {
        return expenseDao.getDayWiseRecords(daySt);
    }

    public Flowable<Double> getTotalDayExpense(Date date) {
        return expenseDao.getTotalDayExpense(date);
    }

    public Flowable<Double> getTotalDayIncome(Date date) {
        return expenseDao.getTotalDayIncome(date);
    }

}
