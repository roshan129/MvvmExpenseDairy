package com.roshanadke.mvvmexpensedairy.data.local

import androidx.room.TypeConverter
import com.roshanadke.mvvmexpensedairy.domain.model.TransactionType
import java.util.Date


class ExpenseTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time


    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return enumValueOf(value)
    }


}