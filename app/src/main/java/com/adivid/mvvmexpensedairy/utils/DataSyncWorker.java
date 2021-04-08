package com.adivid.mvvmexpensedairy.utils;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.core.Scheduler;

@HiltWorker
public class DataSyncWorker extends Worker {

    private final ExpenseDao expenseDao;

    @AssistedInject
    public DataSyncWorker(@Assisted Context context,
                          @Assisted WorkerParameters workerParams,
                          ExpenseDao expenseDao) {
        super(context, workerParams);
        this.expenseDao = expenseDao;
    }

    @NonNull
    @Override
    public Result doWork() {
        init();

        return Result.success();
    }

    private void init() {
        getDataToSyncFromDb();
    }

    private void getDataToSyncFromDb() {

    }
}
