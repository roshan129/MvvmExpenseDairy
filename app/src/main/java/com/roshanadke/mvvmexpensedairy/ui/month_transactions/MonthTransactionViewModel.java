package com.roshanadke.mvvmexpensedairy.ui.month_transactions;

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
public class MonthTransactionViewModel extends ViewModel {

    private MonthTransactionRepository repository;
    private CompositeDisposable compositeDisposable;

    public MutableLiveData<List<ExpenseEntity>> monthlyExpenseEntities = new MutableLiveData<>();
    public MutableLiveData<String> monthlyExpense;
    public MutableLiveData<String> monthlyIncome;

    @Inject
    public MonthTransactionViewModel(MonthTransactionRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        monthlyExpense = new MutableLiveData<>();
        monthlyIncome = new MutableLiveData<>();
    }

    public void getMonthlyRecords(Date firstDay, Date lastDay) {
        compositeDisposable.add(
                repository.getMonthlyRecords(firstDay, lastDay)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                            monthlyExpenseEntities.postValue(expenseEntities);
                        }, throwable -> {
                            Timber.d("getMonthlyRecords exception: %s", throwable.toString());
                        })
        );
    }

    public void getTotalMonthExpenseIncome(Date firstMonthDay, Date lastMonthDay){
        compositeDisposable.add(
                repository.getMonthlyExpense(firstMonthDay, lastMonthDay)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aDouble -> {
                            monthlyExpense.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                        }, throwable -> {
                            Timber.d("getTotalMonthExpenseIncome exception: %s", throwable.toString());
                        })
        );

        compositeDisposable.add(
                repository.getMonthlyIncome(firstMonthDay, lastMonthDay)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aDouble -> {
                            monthlyIncome.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                        }, throwable -> {
                            Timber.d("getTotalMonthExpenseIncome exception: %s", throwable.toString());
                        })
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
