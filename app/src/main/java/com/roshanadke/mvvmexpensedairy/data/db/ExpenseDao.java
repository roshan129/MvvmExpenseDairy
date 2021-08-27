package com.roshanadke.mvvmexpensedairy.data.db;

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
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ExpenseDao {

    @Insert
    Maybe<Long> insertTransaction(ExpenseEntity expense);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insertExpenseList(List<ExpenseEntity> expense);

    @Update
    Maybe<Integer> updateTransaction(ExpenseEntity expenseEntity);

    @Query("Update expenseentity SET isDeleted = 1 WHERE id = :id")
    Single<Integer> deleteTransaction(int id);

    @Query("Delete FROM expenseentity WHERE id =:id")
    Single<Integer> deleteRecordUsingId(int id);

    @Query("Select * from expenseentity WHERE isDeleted = 0 ORDER BY date DESC")
    LiveData<List<ExpenseEntity>> getAllTransactions();

    @Query("Select * from expenseentity WHERE isDeleted = 0 ORDER BY id DESC")
    Flowable<List<ExpenseEntity>> getAllMainTransactions();

    @Query("Select * from expenseentity WHERE isDeleted = 0 ORDER BY id DESC LIMIT 4")
    LiveData<List<ExpenseEntity>> getAllRecentTransactions();

    //Day
    @Query("Select * from expenseentity WHERE isDeleted = 0 AND date BETWEEN :dayStart AND :dayEnd ORDER BY date DESC")
    Flowable<List<ExpenseEntity>> getDayWiseRecords(Date dayStart, Date dayEnd);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND isDeleted = 0 AND date BETWEEN :dayStart AND :dayEnd")
    Flowable<Double> getTotalDayExpense(Date dayStart, Date dayEnd);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Income' AND isDeleted = 0 AND date BETWEEN :dayStart AND :dayEnd")
    Flowable<Double> getTotalDayIncome(Date dayStart, Date dayEnd);

    //week
    @Query("Select * from expenseentity WHERE isDeleted = 0 AND date BETWEEN :firstWeekDay AND :lastWeekDay ORDER BY current_time_millis desc")
    Flowable<List<ExpenseEntity>> getWeeklyWiseRecords(Date firstWeekDay, Date lastWeekDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND isDeleted = 0 AND date BETWEEN :firstWeekDay AND :lastWeekDay")
    Flowable<Double> getTotalWeekExpense(Date firstWeekDay, Date lastWeekDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE date BETWEEN :firstWeekDay AND :lastWeekDay AND transaction_type = 'Income' AND isDeleted = 0")
    Flowable<Double> getTotalWeekIncome(Date firstWeekDay, Date lastWeekDay);

    //month
    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay AND isDeleted = 0 ORDER BY id desc")
    Flowable<List<ExpenseEntity>> getMonthWiseRecords(Date firstDay, Date lastDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND isDeleted = 0")
    Flowable<Double> getTotalMonthExpense(Date firstMonthDay, Date lastMonthDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Income' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND isDeleted = 0")
    Flowable<Double> getTotalMonthIncome(Date firstMonthDay, Date lastMonthDay);

    //month expense income list
    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay AND isDeleted = 0 AND transaction_type = :expenseIncome ORDER BY id desc")
    Flowable<List<ExpenseEntity>> getMonthWiseRecordsExpenseIncome(Date firstDay, Date lastDay, String expenseIncome);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND isDeleted = 0")
    Flowable<Double> getTotalMonthExpenseExpenseIncome(Date firstMonthDay, Date lastMonthDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Income' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND isDeleted = 0")
    Flowable<Double> getTotalMonthIncomeExpenseIncome(Date firstMonthDay, Date lastMonthDay);

    //year
    @Query("Select * from expenseentity WHERE isDeleted = 0 AND date BETWEEN :firstDay AND :lastDay ORDER BY id desc")
    Flowable<List<ExpenseEntity>> getYearWiseRecords(Date firstDay, Date lastDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND isDeleted = 0 AND date BETWEEN :firstDay AND :lastDay")
    Flowable<Double> getYearlyExpense(Date firstDay, Date lastDay);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Income' AND isDeleted = 0 AND date BETWEEN :firstDay AND :lastDay")
    Flowable<Double> getYearlyIncome(Date firstDay, Date lastDay);

    // total
    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND isDeleted = 0")
    LiveData<Double> getExpenseCount();

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Income' AND isDeleted = 0")
    LiveData<Double> getIncomeCount();

    /////  offset change /////////////////////////////////////////////////

    @Query("Select * from expenseentity WHERE isDeleted = 0 ORDER BY id DESC LIMIT 10 OFFSET :offset")
    Observable<List<ExpenseEntity>> getAllMainTransactionsOffset(int offset);

    @Query("Select * from expenseentity WHERE isDeleted = 0 AND date BETWEEN :firstWeekDay AND :lastWeekDay LIMIT 10 OFFSET :offset")
    Flowable<List<ExpenseEntity>> getWeeklyWiseRecordsOffset(
            Date firstWeekDay, Date lastWeekDay, int offset);

    //custom
    @Query("Select * from expenseentity WHERE date BETWEEN :firstDay AND :lastDay AND transaction_category LIKE '%' || :category || '%' AND payment_type LIKE '%' || :paymentMode || '%' AND isDeleted = 0")
    Flowable<List<ExpenseEntity>> getCustomRecords(Date firstDay, Date lastDay, String category, String paymentMode);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Expense' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND transaction_category LIKE '%' || :category || '%' AND payment_type LIKE '%' || :paymentMode || '%' AND isDeleted = 0")
    Flowable<Double> getCustomExpense(Date firstMonthDay, Date lastMonthDay, String category, String paymentMode);

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transaction_type = 'Income' AND date BETWEEN :firstMonthDay AND :lastMonthDay AND transaction_category LIKE '%' || :category || '%' AND payment_type LIKE '%' || :paymentMode || '%' AND isDeleted = 0")
    Flowable<Double> getCustomIncome(Date firstMonthDay, Date lastMonthDay, String category, String paymentMode);

    //sync
    @Query("Select * from expenseentity WHERE isDataSent = 0 AND isUpdated = 0 AND isDeleted = 0 ORDER BY date DESC")
    Maybe<List<ExpenseEntity>> getDataToSync();

    @Query("Select * from expenseentity WHERE isDataSent = 0 AND isUpdated = 1 AND isDeleted = 0 ORDER BY date DESC")
    Maybe<List<ExpenseEntity>> getUpdatedDataToSync();

/*    //check offline data
    @Query("Select COUNT(id) from expenseentity WHERE isDataSent = 0 AND isUpdated = 0 AND isDeleted = 0 ORDER BY date DESC")
    Maybe<List<ExpenseEntity>> checkOfflineRecords();*/

    //deleted data
    @Query("Select * from expenseentity WHERE isDeleted = 1 ORDER BY date DESC")
    Maybe<List<ExpenseEntity>> getDeletedRecords();

}
