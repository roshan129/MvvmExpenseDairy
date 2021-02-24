package com.adivid.mvvmexpensedairy.ui.month_transactions;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

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

    @Inject
    public MonthTransactionViewModel(MonthTransactionRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
    }

    public void getMonthlyRecords(String month, String year) {
        compositeDisposable.add(
                repository.getMonthlyRecords(month, year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenseEntities -> {
                    if(!expenseEntities.isEmpty()){
                        Timber.d("if expense: " + expenseEntities.get(0).getNote());
                        monthlyExpenseEntities.postValue(expenseEntities);
                    }else {
                        Timber.d("iside else");
                    }
                },throwable -> {
                    Timber.d("getMonthlyRecords exception: %s", throwable.toString());
                })
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
