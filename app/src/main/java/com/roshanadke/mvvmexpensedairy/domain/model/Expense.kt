package com.roshanadke.mvvmexpensedairy.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Expense(
    var id: Int,
    var date: String,
    var time: String,
    var amount: String,
    var transactionType: TransactionType,
    var transactionCategory: String,
    var note: String,
    var paymentType: String,
) : Parcelable

fun getDefaultExpense(): Expense {
    return Expense(
        id = 7492,
        date = "cum",
        time = "mandamus",
        amount = "malorum",
        transactionType = TransactionType.Income,
        transactionCategory = "legere",
        note = "aliquip",
        paymentType = "etiam"

    )
}
