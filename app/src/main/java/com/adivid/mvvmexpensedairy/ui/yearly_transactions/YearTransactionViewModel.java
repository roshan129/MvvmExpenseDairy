package com.adivid.mvvmexpensedairy.ui.yearly_transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.domain.Expense;

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
public class YearTransactionViewModel extends ViewModel {

    private final YearTransactionsRepository repository;
    private final CompositeDisposable compositeDisposable;
    public MutableLiveData<List<ExpenseEntity>> yearlyExpenseTransactions;

    @Inject
    public YearTransactionViewModel(YearTransactionsRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        yearlyExpenseTransactions = new MutableLiveData<>();
    }

    public void getYearlyRecords(Date firstDay, Date lastDay) {
        compositeDisposable.add(
                repository.getYearlyRecords(firstDay, lastDay)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                            yearlyExpenseTransactions.postValue(expenseEntities);
                        }, throwable -> {
                            Timber.d("exception: " + throwable);
                        })
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
