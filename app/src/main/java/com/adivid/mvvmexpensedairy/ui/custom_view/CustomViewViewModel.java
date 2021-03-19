package com.adivid.mvvmexpensedairy.ui.custom_view;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@HiltViewModel
public class CustomViewViewModel extends ViewModel {

    private CustomViewRepository repository;
    private final CompositeDisposable compositeDisposable;

    @Inject
    public CustomViewViewModel(CustomViewRepository repository) {
        this.repository = repository;
        compositeDisposable = new CompositeDisposable();
    }


}
