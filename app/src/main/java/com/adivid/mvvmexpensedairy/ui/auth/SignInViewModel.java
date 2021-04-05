package com.adivid.mvvmexpensedairy.ui.auth;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class SignInViewModel extends ViewModel {

    private final SignInRepository repository;
    private final CompositeDisposable compositeDisposable;

    private MutableLiveData<FirebaseUser> firebaseUser;

    @Inject
    public SignInViewModel(SignInRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
    }

    public void firebaseAuthWithGoogle(String idToken) {
        Timber.d("inside viewmodel");
        Observable.fromCallable(repository.firebaseAuthWithGoogle(idToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        /*Observable.just(repository.firebaseAuthWithGoogle(idToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(firebaseUserSingle -> {
                    FirebaseUser user = firebaseUserSingle.blockingGet();
                    Timber.d("user: " + user.getEmail());
                }, throwable -> {
                    Timber.d("firebaseAuthWithGoogle exception: %s", throwable.toString());
                });*/


    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
