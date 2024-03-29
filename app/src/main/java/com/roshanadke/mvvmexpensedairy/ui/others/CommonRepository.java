package com.roshanadke.mvvmexpensedairy.ui.others;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class CommonRepository {

    private final ExpenseDao expenseDao;

    @Inject
    public CommonRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public Single<Integer> deleteRecordFromDb(ExpenseEntity expenseEntity) {
        int id = expenseEntity.getId();
        return expenseDao.deleteTransaction(id);
    }
}
