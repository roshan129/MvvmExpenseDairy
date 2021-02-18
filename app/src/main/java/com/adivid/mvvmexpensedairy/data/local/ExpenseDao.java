package com.adivid.mvvmexpensedairy.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;
import com.adivid.mvvmexpensedairy.domain.Expense;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface ExpenseDao {

    @Insert
    Maybe<Long> insertTransaction(ExpenseEntity expense);

    @Query("Select * from expenseentity ORDER BY id DESC")
    LiveData<List<ExpenseEntity>> getAllTransactions();

    @Query("Select * from expenseentity ORDER BY id DESC")
    Flowable<List<ExpenseEntity>> getAllMainTransactions();

    @Query("Select * from expenseentity ORDER BY id DESC LIMIT 4")
    LiveData<List<ExpenseEntity>> getAllRecentTransactions();

    @Query("Select * from expenseentity WHERE date = date('now')")
    LiveData<List<ExpenseEntity>> getDayWiseRecords();

    @Query("Select * from expenseentity WHERE strftime('%Y', date) = :year AND strftime('%m', date) = :monthName")
    Flowable<List<ExpenseEntity>> getMonthWiseRecords(String monthName, String year);

}
