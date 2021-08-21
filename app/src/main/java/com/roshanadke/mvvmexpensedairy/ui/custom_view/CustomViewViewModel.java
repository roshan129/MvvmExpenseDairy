package com.roshanadke.mvvmexpensedairy.ui.custom_view;

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
public class CustomViewViewModel extends ViewModel {

    private final CustomViewRepository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<ExpenseEntity>> customTransactions = new MutableLiveData<>();
    public MutableLiveData<String> customExpenseCount = new MutableLiveData<>();
    public MutableLiveData<String> customIncomeCount = new MutableLiveData<>();

    @Inject
    public CustomViewViewModel(CustomViewRepository repository) {
        this.repository = repository;
    }

    public void getCustomList(Date firstDay, Date lastDay, String category, String paymentMode) {
        compositeDisposable.add(
                repository.getCustomRecords(firstDay, lastDay, category, paymentMode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(expenseEntities -> {
                            Timber.d("in subscribe");
                            /*if (!expenseEntities.isEmpty()) {
                                customTransactions.postValue(expenseEntities);
                            }*/
                            customTransactions.postValue(expenseEntities);
                        }, throwable -> {
                            Timber.d("getCustomList exception: %s", throwable.toString());
                        })
        );
    }

    public void getCustomExpenseIncomeCount(Date firstDay, Date lastDay,
                                            String category, String paymentMode) {
        compositeDisposable.add(
                repository.getCustomExpense(firstDay, lastDay, category, paymentMode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aDouble -> {
                            customExpenseCount.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                        }, throwable -> {
                            Timber.d("getCustomExpenseIncomeCount: exception: %s", throwable.toString());
                        })
        );

        compositeDisposable.add(
                repository.getCustomIncome(firstDay, lastDay, category, paymentMode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aDouble -> {
                            customIncomeCount.postValue(Utils.convertToDecimalFormat(String.valueOf(aDouble)));
                        }, throwable -> {
                            Timber.d("getCustomExpenseIncomeCount: exception: %s", throwable.toString());
                        })
        );
    }

}
