package com.roshanadke.mvvmexpensedairy.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ExpenseEntity(

    @PrimaryKey
    private var id: Int,
    private var uniqueId: String,
    private var date: Date,
    private var time: String,
    private var amount: String,
    private var transactionType: String,
    private var transactionCategory: String,
    private var note: String,
    private var paymentType: String,

)
