package com.adivid.mvvmexpensedairy.ui.day_transactions;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.utils.Utils;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class DayTransactionViewModel extends ViewModel {

    private DayTransactionRepository repository;
    private CompositeDisposable compositeDisposable;
    public MutableLiveData<List<ExpenseEntity>> dayTransactions = new MutableLiveData<>();

    @Inject
    public DayTransactionViewModel(DayTransactionRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        init();
    }

    private void init() {


    }

    public void getDayWiseRecords(String date) {
        compositeDisposable.add(
                repository.getDayWiseRecords(Utils.convertToStoringDate(date))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenseEntities -> {
                    dayTransactions.postValue(expenseEntities);
                },throwable -> {
                    Timber.d("exception:getDayWiseRecords: " + throwable);
                })
        );


    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
