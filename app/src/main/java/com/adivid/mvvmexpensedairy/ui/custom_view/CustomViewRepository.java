package com.adivid.mvvmexpensedairy.ui.custom_view;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

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

}
