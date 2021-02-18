package com.adivid.mvvmexpensedairy.ui.all_transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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
    //public MutableLiveData<List<ExpenseEntity>> allTransactions = new MutableLiveData<>();

    public LiveData<List<ExpenseEntity>> allTransactions;

    @Inject
    public AllTransactionsViewModel(AllTransactionsRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        init();
    }

    private void init() {
        allTransactions = getAllTransactions();
    }

    public LiveData<List<ExpenseEntity>> getAllTransactions() {
       return repository.getAllTransactions();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
