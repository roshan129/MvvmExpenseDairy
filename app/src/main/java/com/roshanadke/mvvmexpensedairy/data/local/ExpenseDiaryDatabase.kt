package com.roshanadke.mvvmexpensedairy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ExpenseTypeConverter::class)
abstract class ExpenseDiaryDatabase: RoomDatabase() {
    abstract fun getExpenseDao(): ExpenseDao

}