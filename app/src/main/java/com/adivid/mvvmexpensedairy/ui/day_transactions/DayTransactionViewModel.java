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

    private final DayTransactionRepository repository;
    private final CompositeDisposable compositeDisposable;
    public MutableLiveData<List<ExpenseEntity>> dayTransactions = new MutableLiveData<>();
    public MutableLiveData<String> dayExpenseCount = new MutableLiveData<>();
    public MutableLiveData<String> dayIncomeCount = new MutableLiveData<>();

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


    public void getTotalDayExpenseIncome(String date) {
        compositeDisposable.add(
                repository.getTotalDayExpense(Utils.convertToStoringDate(date))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aDouble -> {
                    dayExpenseCount.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                }, throwable -> {
                    Timber.d("exception: getTotalDayExpense: %s", throwable.toString());
                })
        );

        compositeDisposable.add(
                repository.getTotalDayIncome(Utils.convertToStoringDate(date))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aDouble -> {
                            dayIncomeCount.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                        }, throwable -> {
                            Timber.d("exception: getTotalDayExpense: %s", throwable.toString());
                        })
        );
    }

    public void getTotalDayIncome(String today) {

    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
