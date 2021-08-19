package com.adivid.mvvmexpensedairy.ui.expense_income;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class ExpenseIncomeRepository {

    private final ExpenseDao expenseDao;

    @Inject
    public ExpenseIncomeRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Flowable<List<ExpenseEntity>> getMonthlyRecords(Date firstDay, Date lastDay, String stringExpenseIncome) {
        return expenseDao.getMonthWiseRecordsExpenseIncome(firstDay, lastDay, stringExpenseIncome);
    }

    public Flowable<Double> getMonthlyExpense(Date firstDay, Date lastDay) {
        return expenseDao.getTotalMonthExpense(firstDay, lastDay);
    }

    public Flowable<Double> getMonthlyIncome(Date firstDay, Date lastDay) {
        return expenseDao.getTotalMonthIncome(firstDay, lastDay);
    }

}
