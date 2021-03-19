package com.adivid.mvvmexpensedairy.ui.custom_view;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;

import javax.inject.Inject;

public class CustomViewRepository {


    private ExpenseDao expenseDao;

    @Inject
    public CustomViewRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }


}
