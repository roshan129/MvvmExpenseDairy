package com.roshanadke.mvvmexpensedairy.domain.model

import java.util.Date

data class Expense(
    var id: Int,
    var date: String,
    var time: String,
    var amount: String,
    var transactionType: TransactionType,
    var transactionCategory: String,
    var note: String,
    var paymentType: String,
)
