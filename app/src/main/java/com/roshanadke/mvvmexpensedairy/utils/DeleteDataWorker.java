package com.roshanadke.mvvmexpensedairy.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.google.firebase.auth.FirebaseAuth;
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
public class DeleteDataWorker extends Worker {

    private final ExpenseDao expenseDao;
    private final CompositeDisposable compositeDisposable;
    private FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private String firebaseUId;

    @AssistedInject
    public DeleteDataWorker(@Assisted Context context,
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
        getDeletedRecordsFromDb();
    }

    private void getDeletedRecordsFromDb() {
        compositeDisposable.add(
                expenseDao.getDeletedRecords()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                                    Timber.d("inside  getDeletedRecordsFromDb: " + expenseEntities.size());
                                    setUpDataToDelete(expenseEntities);
                                },
                                throwable -> Timber.d("exception1: %s", throwable.toString()))
        );
    }

    private void setUpDataToDelete(List<ExpenseEntity> expenseEntities) {
        Disposable d = Observable.fromIterable(expenseEntities)
                .map(expenseEntity -> expenseEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                            Timber.d("inside setUpDataToDelete");
                            deleteDataFromServer(entity);
                        },
                        throwable -> Timber.d("exception2: %s", throwable.toString()));
        compositeDisposable.add(d);

    }

    private void deleteDataFromServer(ExpenseEntity entity) {
        Timber.d("inside deleteDataFromServer");
        String docId = entity.getDocId();
        if (docId != null && !docId.isEmpty()) {
            firebaseFirestore.collection("user_data")
                    .document(firebaseUId).collection("expense_data")
                    .document(docId)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Timber.d("Document deleted successfully");
                    })
                    .addOnFailureListener(e -> {
                        Timber.d("exception: %s", e.getMessage());
                    });
        }

    }
}
