package com.adivid.mvvmexpensedairy.di;

import android.content.Context;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseDiaryDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class AppModule {

    @Singleton
    @Provides
    public ExpenseDiaryDatabase provideExpenseDb(@ApplicationContext Context context){
        return ExpenseDiaryDatabase.getInstance(context);
    }

    @Singleton
    @Provides
    public ExpenseDao provideExpenseDao(ExpenseDiaryDatabase db){
        return db.getExpenseDao();
    }



}
