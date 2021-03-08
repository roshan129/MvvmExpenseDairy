package com.adivid.mvvmexpensedairy.ui.all_transactions;

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
public class AllTransactionsViewModel extends ViewModel {

    private final AllTransactionsRepository repository;
    private final CompositeDisposable compositeDisposable;

    public MutableLiveData<List<ExpenseEntity>> allTransactions;

    @Inject
    public AllTransactionsViewModel(AllTransactionsRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
        allTransactions = new MutableLiveData<>();
    }

    public void getAllTransactions(int offset) {
        compositeDisposable.add(
                repository.getAllTransactionsOffset(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(expenseEntities -> {
                    allTransactions.postValue(expenseEntities);
                }, throwable -> {
                    Timber.d("exception: getAllTransactionOffset: %s", throwable.toString());
                })
        );
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
