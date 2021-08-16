package com.adivid.mvvmexpensedairy.ui.dashboard;

import androidx.lifecycle.LiveData;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class DashboardRepository {

    private final ExpenseDao expenseDao;

    @Inject
    public DashboardRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Flowable<List<ExpenseEntity>> getAllMainTransactions() {
        return expenseDao.getAllMainTransactions();
    }

    public LiveData<List<ExpenseEntity>> getAllRecentTransactions() {
        return expenseDao.getAllRecentTransactions();
    }

    public LiveData<Double> getExpenseCount() {
        return expenseDao.getExpenseCount();
    }

    public LiveData<Double> getIncomeCount() {
        return expenseDao.getIncomeCount();
    }

    public Maybe<List<ExpenseEntity>> checkOfflineRecords() {
        return expenseDao.getDataToSync();
    }

    public Single<Integer> deleteRecordFromDb(ExpenseEntity expenseEntity) {
        int id = expenseEntity.getId();
        return expenseDao.deleteTransaction(id);
    }


}
