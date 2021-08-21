package com.roshanadke.mvvmexpensedairy.ui.weekly_transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.utils.Utils;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class WeekTransactionViewModel extends ViewModel {

    private WeekTransactionRepository repository;
    private CompositeDisposable compositeDisposable;
    public MutableLiveData<List<ExpenseEntity>> weeklyTransactions;
    public MutableLiveData<String> weeklyExpense;
    public MutableLiveData<String> weeklyIncome;

    @Inject
    public WeekTransactionViewModel(WeekTransactionRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        weeklyTransactions = new MutableLiveData<>();
        weeklyExpense = new MutableLiveData<>();
        weeklyIncome = new MutableLiveData<>();
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

    public void getWeeklyReportsOffset(Date firstWeekDay, Date lastWeekDay, int offset) {
        compositeDisposable.add(
                repository.getWeeklyReportsOffset(firstWeekDay, lastWeekDay, offset)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                            weeklyTransactions.postValue(expenseEntities);
                        },throwable -> {
                            Timber.d("exception: %s", throwable.toString());
                        })
        );
    }

    public void getWeekExpenseIncome(Date firstWeekDay, Date lastWeekDay){
        compositeDisposable.add(
          repository.getWeekExpense(firstWeekDay, lastWeekDay)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aDouble -> {
                    weeklyExpense.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                }, throwable -> {
                    Timber.d("exception: %s", throwable.toString());
                })
        );

        compositeDisposable.add(
                repository.getWeekIncome(firstWeekDay, lastWeekDay)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aDouble -> {
                            weeklyIncome.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                        }, throwable -> {
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
