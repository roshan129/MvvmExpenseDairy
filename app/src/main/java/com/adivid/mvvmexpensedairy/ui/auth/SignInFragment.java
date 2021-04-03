package com.adivid.mvvmexpensedairy.ui.auth;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.databinding.FragmentSignInBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;

    public SignInFragment() {
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSignInBinding.bind(view);

        init();
    }

    private void init() {

    }

}
