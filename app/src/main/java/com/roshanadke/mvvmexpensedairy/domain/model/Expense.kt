package com.roshanadke.mvvmexpensedairy.domain.model

data class Expense(
    var id: Int,
    var date: String,
    var time: String,
    var amount: String,
    var transactionType: String,
    var transactionCategory: String,
    var note: String,
    var paymentType: String,
)
