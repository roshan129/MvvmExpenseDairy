package com.roshanadke.mvvmexpensedairy.domain.repository

import com.roshanadke.mvvmexpensedairy.data.local.ExpenseEntity
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun insertTransaction(expenseEntity: ExpenseEntity)

    fun getAllExpenseList(): Flow<List<Expense>>

}