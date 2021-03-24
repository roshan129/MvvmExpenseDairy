package com.adivid.mvvmexpensedairy.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface ExpenseDao {

    @Insert
    Maybe<Long> insertTransaction(ExpenseEntity expense);

    @Update
    Maybe<Integer> updateTransaction(ExpenseEntity expenseEntity);

    @Query("Select * from expenseentity ORDER BY id DESC")
    LiveData<List<ExpenseEntity>> getAllTransactions();

    @Query("Select * from expenseentity ORDER BY id DESC")
    Flowable<List<ExpenseEntity>> getAllMainTransactions();

    @Query("Select * from expenseentity ORDER BY id DESC LIMIT 4")
    LiveData<List<ExpenseEntity>> getAllRecentTransactions();


    //Day
    @Query("Select * from expenseentity WHERE date = :daySt")
    Flowable<List<ExpenseEntity>> getDayWiseRecords(Date daySt);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Expense' AND date = :daySt")
    Flowable<Double> getTotalDayExpense(Date daySt);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Income' AND date = :daySt")
    Flowable<Double> getTotalDayIncome(Date daySt);


    //week
    @Query("Select * from expenseentity WHERE date BETWEEN :firstWeekDay AND :lastWeekDay")
    Flowable<List<ExpenseEntity>> getWeeklyWiseRecords(Date firstWeekDay, Date lastWeekDay);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstWeekDay AND :lastWeekDay")
    Flowable<Double> getTotalWeekExpense(Date firstWeekDay, Date lastWeekDay);

    @Query("Select SUM(amount) from expenseentity WHERE date BETWEEN :firstWeekDay AND :lastWeekDay AND transaction_type = 'Income'")
    Flowable<Double> getTotalWeekIncome(Date firstWeekDay, Date lastWeekDay);

    //month
    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay")
    Flowable<List<ExpenseEntity>> getMonthWiseRecords(Date firstDay, Date lastDay);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstMonthDay AND :lastMonthDay")
    Flowable<Double> getTotalMonthExpense(Date firstMonthDay, Date lastMonthDay);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Income' AND date BETWEEN :firstMonthDay AND :lastMonthDay")
    Flowable<Double> getTotalMonthIncome(Date firstMonthDay, Date lastMonthDay);

    //year
    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay ORDER BY id desc")
    Flowable<List<ExpenseEntity>> getYearWiseRecords(Date firstDay, Date lastDay);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstDay AND :lastDay ORDER BY id desc")
    Flowable<Double> getYearlyExpense(Date firstDay, Date lastDay);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Income' AND date BETWEEN :firstDay AND :lastDay ORDER BY id desc")
    Flowable<Double> getYearlyIncome(Date firstDay, Date lastDay);

    // total
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

    //custom
    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay AND transaction_category = :category AND payment_type = :paymentMode")
    Flowable<List<ExpenseEntity>> getCustomRecords(Date firstDay, Date lastDay, String category, String paymentMode);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND transaction_category = :category AND payment_type = :paymentMode")
    Flowable<Double> getCustomExpense(Date firstMonthDay, Date lastMonthDay, String category, String paymentMode);

    @Query("Select SUM(amount) from expenseentity WHERE transaction_type = 'Income' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND transaction_category = :category AND payment_type = :paymentMode")
    Flowable<Double> getCustomIncome(Date firstMonthDay, Date lastMonthDay, String category, String paymentMode);


}
