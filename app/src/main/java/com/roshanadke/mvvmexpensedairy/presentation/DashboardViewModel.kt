package com.roshanadke.mvvmexpensedairy.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseDao
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseDiaryDatabase
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseEntity
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    expenseRepository: ExpenseRepository
): ViewModel() {

    var expenselist = mutableStateListOf<Expense>()

    init {

 /*       val expense = ExpenseEntity(
            uniqueId = "1234556",
            date = Date(System.currentTimeMillis()),
            time = "19:50",
            amount ="100",
            transactionType = "consectetuer",
            transactionCategory = "repudiandae",
            note = "dolore",
            paymentType = "ultrices",
        )*/

        viewModelScope.launch {
           /* expenseDao.insertTransaction(
                expense
            )
            expenseDao.insertTransaction(
                expense
            )*/
            expenseRepository.getAllExpenseList().collect {
                Log.d("TAG", "list: ${it[0].transactionType} ")
                expenselist.addAll(it.toMutableList())
            }
        }

    }

}