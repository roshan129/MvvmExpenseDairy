package com.roshanadke.mvvmexpensedairy.ui.add_transactions;


import androidx.lifecycle.ViewModel;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class AddTransactionViewModel extends ViewModel {

    private AddTransactionRepository repository;
    private CompositeDisposable compositeDisposable;

    @Inject
    public AddTransactionViewModel(AddTransactionRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
    }

    public void insertTransaction(ExpenseEntity expenseEntity){
        compositeDisposable.add(
                repository.insertTransaction(expenseEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    Timber.d("insertTransaction: " + aLong);
                },throwable -> {
                    Timber.d("excpetion: %s", throwable.getMessage());
                })
        );
    }

    public void updateTransaction(ExpenseEntity expenseEntity){
        compositeDisposable.add(
                repository.updateTransaction(expenseEntity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            Timber.d("updateTransaction: " + aLong);
                        },throwable -> {
                            Timber.d("updateTransaction excpetion: %s", throwable.getMessage());
                        })
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}






