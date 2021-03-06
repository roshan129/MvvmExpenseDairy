package com.adivid.mvvmexpensedairy.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
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

   /* @Query("Select * from expenseentity WHERE date between :daySt AND :dayEnd")
    LiveData<List<ExpenseEntity>> getDayWiseRecords(Date daySt, Date dayEnd);*/

    @Query("Select * from expenseentity WHERE date = :daySt")
    Flowable<List<ExpenseEntity>> getDayWiseRecords(Date daySt);

    @Query("Select * from expenseentity WHERE date BETWEEN :firstWeekDay AND :lastWeekDay")
    Flowable<List<ExpenseEntity>> getWeeklyWiseRecords(Date firstWeekDay, Date lastWeekDay);

    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay")
    Flowable<List<ExpenseEntity>> getMonthWiseRecords(Date firstDay, Date lastDay);

    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay")
    Flowable<List<ExpenseEntity>> getYearWiseRecords(Date firstDay, Date lastDay);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Expense'")
    LiveData<Double> getExpenseCount();

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Income'")
    LiveData<Double> getIncomeCount();

    /////  offset change /////////////////////////////////////////////////

    @Query("Select * from expenseentity ORDER BY id DESC LIMIT 10 OFFSET :offset")
    Flowable<List<ExpenseEntity>> getAllMainTransactionsOffset(int offset);

    @Query("Select * from expenseentity WHERE date BETWEEN :firstWeekDay AND :lastWeekDay LIMIT 10 OFFSET :offset")
    Flowable<List<ExpenseEntity>> getWeeklyWiseRecordsOffset(
            Date firstWeekDay, Date lastWeekDay, int offset);

}
