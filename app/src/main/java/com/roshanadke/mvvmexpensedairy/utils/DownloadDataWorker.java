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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltWorker
public class DownloadDataWorker extends Worker {

    private final ExpenseDao expenseDao;
    private final CompositeDisposable compositeDisposable;
    private FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private String firebaseUId;

    @AssistedInject
    public DownloadDataWorker(@Assisted Context context,
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
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {

        firebaseFirestore.collection("user_data")
                .document(firebaseUId)
                .collection("expense_data")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<FirebaseExpenseDto> firebaseExpenseList =
                                queryDocumentSnapshots.toObjects(FirebaseExpenseDto.class);

                        List<ExpenseEntity> list = new ArrayList<>();
                        for (int i = 0; i < firebaseExpenseList.size(); i++) {
                            ExpenseEntity entity = new FirebaseExpenseMapper()
                                    .mapFromDomainModel(firebaseExpenseList.get(i));
                            entity.setDataSent(true);
                            list.add(entity);
                        }
                        insertServerDataInDb(list);

                    } else {
                        Timber.d("empty list");
                    }
                })
                .addOnFailureListener(e -> {
                    Timber.d("exception getDataFromFirebase: %s", e.getMessage());
                });

    }

    private void insertServerDataInDb(List<ExpenseEntity> list) {
        compositeDisposable.add(
                expenseDao.insertExpenseList(list).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            Timber.d("data insert success");
                        }, throwable -> {
                            Timber.d("exception insertServerDataInDb: %s", throwable.getMessage());
                        })
        );
    }


}
