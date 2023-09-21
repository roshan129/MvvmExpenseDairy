package com.roshanadke.mvvmexpensedairy.domain.model

import com.roshanadke.mvvmexpensedairy.data.local.ExpenseEntity
import kotlin.math.exp

object TransactionValidator {

    fun validateTransaction(expense: ExpenseEntity): ValidationResult {
        var result = ValidationResult()

        if(expense.amount.isEmpty()) {
            result = result.copy(amountError = "Please Enter Amount")
        }

        return result
    }

    data class ValidationResult(
        val amountError: String? = null,
    )

}