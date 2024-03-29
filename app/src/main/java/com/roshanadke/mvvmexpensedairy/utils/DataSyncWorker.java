package com.roshanadke.mvvmexpensedairy.utils;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.domain.FirebaseExpenseDto;
import com.roshanadke.mvvmexpensedairy.domain.mapper.FirebaseExpenseMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltWorker
public class DataSyncWorker extends Worker {

    private final ExpenseDao expenseDao;
    private final CompositeDisposable compositeDisposable;
    private FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private String firebaseUId;

    @AssistedInject
    public DataSyncWorker(@Assisted Context context,
                          @Assisted WorkerParameters workerParams,
                          ExpenseDao expenseDao,
                          FirebaseAuth firebaseAuth) {
        super(context, workerParams);
        compositeDisposable = new CompositeDisposable();
        this.expenseDao = expenseDao;
        this.firebaseAuth = firebaseAuth;
    }

    @NonNull
    @Override
    public Result doWork() {

        init();

        return Result.success();
    }

    private void init() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUId = firebaseAuth.getUid();
        getDataToSyncFromDb();
    }

    private void getDataToSyncFromDb() {
        compositeDisposable.add(
                expenseDao.getDataToSync()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                                    Timber.d("inside  getDataToSyncFromDb: " + expenseEntities.size());
                                    setUpDataToSend(expenseEntities);
                                },
                                throwable -> Timber.d("exception1: %s", throwable.toString()))
        );
    }

    private void setUpDataToSend(List<ExpenseEntity> expenseEntities) {
        Disposable d = Observable.fromIterable(expenseEntities)
                .map(expenseEntity -> expenseEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                            Timber.d("inside setUpDataToSend");
                            sendDataToServer(entity);
                        },
                        throwable -> Timber.d("exception2: %s", throwable.toString()));
        compositeDisposable.add(d);

    }

    private void sendDataToServer(ExpenseEntity entity) {
        Timber.d("inside sendDataToServer");

        if(firebaseUId !=null && !firebaseUId.isEmpty()){
            FirebaseExpenseDto fExpense =
                    new FirebaseExpenseMapper().mapToDomainModel(entity);
            fExpense.setFirebaseUId(firebaseUId);
            DocumentReference documentReference =
                    firebaseFirestore.collection("user_data")
                            .document(firebaseUId).collection("expense_data").document();
            String docId = documentReference.getId();
            fExpense.setDocId(docId);
            documentReference.set(fExpense).addOnSuccessListener(aVoid -> {
                entity.setDataSent(true);
                entity.setFirebaseUid(firebaseUId);
                entity.setDocId(docId);
                expenseDao.updateTransaction(entity).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            }).addOnFailureListener(e ->
                    Timber.d("sendDataToServer exception: %s", e.toString()));
        }

    }

    @Override
    public void onStopped() {
        super.onStopped();
        compositeDisposable.clear();
    }
}
