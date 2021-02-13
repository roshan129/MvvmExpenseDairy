package com.adivid.mvvmexpensedairy.ui.all_transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adivid.mvvmexpensedairy.data.local.ExpenseEntity;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class AllTransactionsViewModel extends ViewModel {

    private AllTransactionsRepository repository;
    private CompositeDisposable compositeDisposable;
    public LiveData<List<ExpenseEntity>> allTransactions;
    public MutableLiveData<List<ExpenseEntity>> _allTransactions = new MutableLiveData<>();

    @Inject
    public AllTransactionsViewModel(AllTransactionsRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        getAllTransactions();
    }

    public void getAllTransactions() {
        compositeDisposable.add(repository.getAllTransactions()
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenseEntities -> {
                    if(!expenseEntities.isEmpty()){
                        _allTransactions.postValue(expenseEntities);
                        allTransactions = _allTransactions;
                    }
                },throwable -> {
                    Timber.d("exception: %s", throwable.getMessage());
                })
        );
    }

}
