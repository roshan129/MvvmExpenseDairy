package com.roshanadke.mvvmexpensedairy.ui.custom_view;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class CustomViewRepository {

    private final ExpenseDao expenseDao;

    @Inject
    public CustomViewRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Flowable<List<ExpenseEntity>> getCustomRecords(Date firstDay, Date lastDay, String category, String paymentMode){
        return expenseDao.getCustomRecords(firstDay, lastDay, category, paymentMode);
    }

    public Flowable<Double> getCustomExpense(Date firstDay, Date lastDay, String category, String paymentMode){
        return expenseDao.getCustomExpense(firstDay, lastDay, category, paymentMode);
    }

    public Flowable<Double> getCustomIncome(Date firstDay, Date lastDay, String category, String paymentMode){
        return expenseDao.getCustomIncome(firstDay, lastDay, category, paymentMode);
    }
}
