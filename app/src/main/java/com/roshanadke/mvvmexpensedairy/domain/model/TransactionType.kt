package com.roshanadke.mvvmexpensedairy.domain.model

enum class TransactionType(type: String) {
    Expense("Expense"),
    Income("Income");

    companion object {
        fun getTransactionType(type: String): TransactionType {
            return if (type == "Expense") {
                Expense
            } else {
                Income
            }
        }
    }

}

