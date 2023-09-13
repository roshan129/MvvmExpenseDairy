package com.roshanadke.mvvmexpensedairy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import java.util.Date

@Entity
data class ExpenseEntity(

    var uniqueId: String,
    var date: Date,
    var time: String,
    var amount: String,
    var transactionType: String,
    var transactionCategory: String,
    var note: String,
    var paymentType: String

) {

    @PrimaryKey
    var id: Int? = null

    fun toExpense(): Expense {
        return Expense(
            id = id ?: 0,
            date = date.toString(),
            time = time,
            amount = amount,
            transactionType = transactionType,
            transactionCategory = transactionCategory,
            note = note,
            paymentType = paymentType

        )
    }

}
