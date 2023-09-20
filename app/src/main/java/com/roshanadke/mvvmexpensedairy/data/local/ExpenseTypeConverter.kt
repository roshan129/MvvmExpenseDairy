package com.roshanadke.mvvmexpensedairy.data.local

import androidx.room.TypeConverter
import com.roshanadke.mvvmexpensedairy.domain.model.CategoryType
import com.roshanadke.mvvmexpensedairy.domain.model.TransactionType
import java.util.Date


class ExpenseTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time


    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.type
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return enumValueOf(value)
    }

    @TypeConverter
    fun fromCategoryType(value: CategoryType): String {
        return value.displayName
    }

    @TypeConverter
    fun toCategoryType(value: String): CategoryType {
        return CategoryType.values().find { it.displayName == value } ?: throw IllegalArgumentException("Invalid CategoryType: $value")
    }

}