package com.roshanadke.mvvmexpensedairy.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseDao
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseEntity
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.domain.model.TransactionType
import com.roshanadke.mvvmexpensedairy.domain.model.TransactionValidator
import com.roshanadke.mvvmexpensedairy.domain.model.getDefaultExpense
import com.roshanadke.mvvmexpensedairy.domain.repository.ExpenseRepository
import com.roshanadke.mvvmexpensedairy.utils.getCurrentDisplayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel  @Inject constructor(
    private val repository: ExpenseRepository,
): ViewModel() {


    private var _selectedDate = mutableStateOf(getCurrentDisplayDate())
    val selectedDate: State<String?> = _selectedDate

    private var _selectedTime = mutableStateOf("16:16")
    val selectedTime: State<String> = _selectedTime

    private var _amount = mutableStateOf("")
    val selectedAmount: State<String> = _amount

    private var _selectedTransactionType = mutableStateOf(TransactionType.Expense)
    val selectedTransactionType: State<TransactionType?> = _selectedTransactionType

    private var _selectedCategory = mutableStateOf("Others")
    val selectedCategory: State<String> = _selectedCategory

    private var _note = mutableStateOf("")
    val note: State<String> = _note

    private var _paymentType = mutableStateOf("Cash")
    val paymentType: State<String> = _paymentType

    private var _amountError = mutableStateOf("")
    val amountError: State<String> = _amountError


    fun insertTransaction(onTransactionInserted: () -> Unit) {
        val expenseEntity = getExpenseEntity()

        val result = TransactionValidator.validateTransaction(expenseEntity)
        val error = listOfNotNull(result.amountError)

        if(error.isEmpty()) {
            _amountError.value = ""
            viewModelScope.launch {
                repository.insertTransaction(expenseEntity)
            }
            onTransactionInserted()
        } else {
            _amountError.value = result.amountError ?: ""
        }


    }

    private fun getExpenseEntity(): ExpenseEntity {
        val uniqueId = System.currentTimeMillis().toString()
        val date = selectedDate.value ?: ""
        val time = selectedTime.value ?: ""
        val amount = selectedAmount.value ?: ""
        val transactionType = selectedTransactionType.value ?: TransactionType.Expense
        val transactionCategory = selectedCategory.value
        val note = note.value.ifEmpty { "Not Specified" }
        val paymentType = paymentType.value

        return ExpenseEntity(
            uniqueId = uniqueId,
            date = date,
            time = time,
            amount = amount,
            transactionType = transactionType,
            transactionCategory = transactionCategory,
            note = note,
            paymentType = paymentType
        )
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setSelectedTime(time: String) {
        _selectedTime.value = time
    }

    fun setSelectedAmount(amount: String) {
        _amountError.value = ""
        _amount.value = amount
    }

    fun setSelectedTransactionType(type: TransactionType) {
        _selectedTransactionType.value = type
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setTransactionNote(note: String) {
        _note.value = note
    }

    fun setPaymentType(note: String) {
        _paymentType.value = note
    }

}