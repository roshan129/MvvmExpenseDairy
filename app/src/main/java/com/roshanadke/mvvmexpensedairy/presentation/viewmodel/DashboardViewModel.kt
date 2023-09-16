package com.roshanadke.mvvmexpensedairy.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    expenseRepository: ExpenseRepository
): ViewModel() {

    var expenseList = mutableStateListOf<Expense>()

    private var _expenseCount = mutableStateOf(0.0)
    val expenseCount: State<Double> = _expenseCount

    private var _incomeCount = mutableStateOf(0.0)
    val incomeCount: State<Double> = _incomeCount

    init {

        viewModelScope.launch {

            expenseRepository.getAllExpenseList().collect {
                expenseList.clear()
                expenseList.addAll(it.toMutableList())
            }
        }

        viewModelScope.launch {
            expenseRepository.getAllExpenseCount().collect {
                _expenseCount.value = it
            }
        }

        expenseRepository.getAllIncomeCount().onEach {
            _incomeCount.value = it
        }.launchIn(viewModelScope)

    }

}