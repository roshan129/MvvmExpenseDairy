package com.adivid.mvvmexpensedairy.ui.expense_income;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.ui.month_transactions.MonthTransactionRepository;
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
public class ExpenseIncomeViewModel extends ViewModel {

    private ExpenseIncomeRepository repository;
    private CompositeDisposable compositeDisposable;

    public MutableLiveData<List<ExpenseEntity>> monthlyExpenseEntities = new MutableLiveData<>();
    public MutableLiveData<List<ExpenseEntity>> monthlyIncomeEntities = new MutableLiveData<>();
    public MutableLiveData<String> monthlyExpense;
    public MutableLiveData<String> monthlyIncome;

    @Inject
    public ExpenseIncomeViewModel(ExpenseIncomeRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        monthlyExpense = new MutableLiveData<>();
        monthlyIncome = new MutableLiveData<>();

    }

    public void getMonthlyRecords(Date firstDay, Date lastDay, String stringExpenseIncome) {
        compositeDisposable.add(
                repository.getMonthlyRecords(firstDay, lastDay, stringExpenseIncome)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                            monthlyExpenseEntities.postValue(expenseEntities);
                        }, throwable -> {
                            Timber.d("getMonthlyRecords exception: %s", throwable.toString());
                        })
        );
    }

    public void getTotalMonthExpenseIncome(Date firstMonthDay, Date lastMonthDay, String stringExpenseIncome){
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
