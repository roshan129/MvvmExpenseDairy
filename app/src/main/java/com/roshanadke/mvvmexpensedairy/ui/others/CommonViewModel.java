package com.roshanadke.mvvmexpensedairy.ui.others;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class CommonViewModel extends ViewModel {

    private final CommonRepository repository;
    private CompositeDisposable compositeDisposable;
    public MutableLiveData<Resource<Integer>> deleteRecord;

    @Inject
    public CommonViewModel(CommonRepository repository) {
        this.repository = repository;

        init();
    }

    private void init() {
        compositeDisposable = new CompositeDisposable();
        deleteRecord = new MutableLiveData<>();

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
