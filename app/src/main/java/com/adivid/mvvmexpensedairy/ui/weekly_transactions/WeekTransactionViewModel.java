package com.adivid.mvvmexpensedairy.ui.weekly_transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class WeekTransactionViewModel extends ViewModel {

    private WeekTransactionRepository repository;
    private CompositeDisposable compositeDisposable;
    public MutableLiveData<List<ExpenseEntity>> weeklyTransactions;

    @Inject
    public WeekTransactionViewModel(WeekTransactionRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        weeklyTransactions = new MutableLiveData<>();
    }

    public void getWeeklyReports(Date firstWeekDay, Date lastWeekDay) {
        compositeDisposable.add(
                repository.getWeeklyReports(firstWeekDay, lastWeekDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenseEntities -> {
                    weeklyTransactions.postValue(expenseEntities);
                },throwable -> {
                    Timber.d("exception: %s", throwable.toString());
                })
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
