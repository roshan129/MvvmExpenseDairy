package com.roshanadke.mvvmexpensedairy.domain.model

import java.util.Date

data class Expense(
    var id: Int,
    var date: Date,
    var time: String,
    var amount: String,
    var transactionType: String,
    var transactionCategory: String,
    var note: String,
    var paymentType: String,
)
