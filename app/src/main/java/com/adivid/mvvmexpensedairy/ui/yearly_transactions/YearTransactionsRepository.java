package com.adivid.mvvmexpensedairy.ui.yearly_transactions;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

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

}
