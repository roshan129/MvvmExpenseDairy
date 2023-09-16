package com.roshanadke.mvvmexpensedairy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertTransaction(expense: ExpenseEntity?)

    @Update
    suspend fun updateTransaction(expense: ExpenseEntity?)

    @Query("SELECT * FROM ExpenseEntity")
    fun getAllList(): Flow<List<ExpenseEntity>>

    //@Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transactionType = 'Expense' AND isDeleted = 0")
    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transactionType = 'Expense'")
    fun getExpenseCount(): Flow<Double>

    @Query("Select COALESCE(SUM(amount), 0) from expenseentity WHERE transactionType = 'Income'")
    fun getIncomeCount(): Flow<Double>


}