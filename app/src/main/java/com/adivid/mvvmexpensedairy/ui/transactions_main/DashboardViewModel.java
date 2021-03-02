package com.adivid.mvvmexpensedairy.ui.transactions_main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class DashboardViewModel extends ViewModel {

    private final DashboardRepository repository;
    private CompositeDisposable compositeDisposable;

    public LiveData<List<ExpenseEntity>> recentAllTransactions;
    public LiveData<Double> expenseCount;
    public LiveData<Double> incomeCount;

    public MutableLiveData<List<ExpenseEntity>> allTransactions;

    @Inject
    public DashboardViewModel(DashboardRepository repository) {
        this.repository = repository;

        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();
        allTransactions = new MutableLiveData<>();
        recentAllTransactions = repository.getAllRecentTransactions();
        expenseCount = repository.getExpenseCount();
        incomeCount = repository.getIncomeCount();
    }

    public void getAllTransactions() {
        if (repository != null) {
            compositeDisposable.add(
                    repository.getAllMainTransactions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(expenseEntities -> {
                        if(!expenseEntities.isEmpty()){
                            allTransactions.postValue(expenseEntities);
                        }
                    }, throwable -> {
                        Timber.d("exception: %s", throwable.getMessage());
                    })
            );
        }else{
            Timber.d("repo is null");
        }
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

}
