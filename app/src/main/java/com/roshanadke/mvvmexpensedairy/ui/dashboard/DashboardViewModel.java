package com.roshanadke.mvvmexpensedairy.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.utils.Resource;

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
    public MutableLiveData<Resource<Integer>> deleteRecord;
    public MutableLiveData<Boolean> checkForOfflineData;

    public MutableLiveData<List<ExpenseEntity>> allTransactions;

    @Inject
    public DashboardViewModel(DashboardRepository repository) {
        this.repository = repository;

        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();
        allTransactions = new MutableLiveData<>();
        deleteRecord = new MutableLiveData<>();
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
                                if (!expenseEntities.isEmpty()) {
                                    allTransactions.postValue(expenseEntities);
                                }
                            }, throwable -> {
                                Timber.d("exception: %s", throwable.getMessage());
                            })
            );
        } else {
            Timber.d("repo is null");
        }
    }

    public void deleteRecordFromDb(ExpenseEntity expenseEntity) {
        deleteRecord.postValue(Resource.loading(null));
        compositeDisposable.add(
                repository.deleteRecordFromDb(expenseEntity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            if (integer != 0) {
                                deleteRecord.postValue(Resource.success(integer));
                            } else {
                                deleteRecord.postValue(Resource.error("Some Error Occurred", null));
                            }
                        }, throwable -> {
                            deleteRecord.postValue(Resource.error(throwable.getMessage(), null));
                            Timber.d("exception: %s", throwable.toString());
                        })
        );
    }



    public void resetDeleteObserver(){
        deleteRecord = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

}
