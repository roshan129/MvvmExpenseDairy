package com.roshanadke.mvvmexpensedairy.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseDao
import com.roshanadke.mvvmexpensedairy.data.local.ExpenseDiaryDatabase
import com.roshanadke.mvvmexpensedairy.data.repository.ExpenseRepositoryImpl
import com.roshanadke.mvvmexpensedairy.domain.repository.ExpenseRepository
import com.roshanadke.mvvmexpensedairy.utils.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseModule {


    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ExpenseDiaryDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideExpenseDao(db: ExpenseDiaryDatabase) = db.getExpenseDao()

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }


}