package com.roshanadke.mvvmexpensedairy.data.local

import androidx.room.TypeConverter
import java.util.Date


class ExpenseTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}