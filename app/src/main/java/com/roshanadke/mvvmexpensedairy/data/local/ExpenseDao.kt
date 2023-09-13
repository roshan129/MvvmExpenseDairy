package com.roshanadke.mvvmexpensedairy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertTransaction(expense: ExpenseEntity?)

    @Update
    suspend fun updateTransaction(expense: ExpenseEntity?)

    @Query("SELECT * FROM ExpenseEntity")
    fun getAllList(): Flow<List<ExpenseEntity>>



}