package com.roshanadke.mvvmexpensedairy.domain.model

data class Expense(
    private var id: Int,
    private var date: String,
    private var time: String,
    private var amount: String,
    private var transactionType: String,
    private var transactionCategory: String,
    private var note: String,
    private var paymentType: String,
)
