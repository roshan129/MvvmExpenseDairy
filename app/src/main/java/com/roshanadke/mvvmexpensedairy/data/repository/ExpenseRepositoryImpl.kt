package com.roshanadke.mvvmexpensedairy.data.repository

import com.roshanadke.mvvmexpensedairy.data.local.ExpenseDao
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseEntity
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao
): ExpenseRepository {

    override suspend fun insertTransaction(expenseEntity: ExpenseEntity) {
        expenseDao.insertTransaction(expenseEntity)
    }

    override fun getAllExpenseList(): Flow<List<Expense>> {
        return expenseDao.getAllTransactions().map { expenseEntityList ->
            expenseEntityList.map { it.toExpense() }
        }
    }

    override fun getAllExpenseCount(): Flow<Double> {
        return expenseDao.getExpenseCount()
    }

    override fun getAllIncomeCount(): Flow<Double> {
        return expenseDao.getIncomeCount()
    }
}