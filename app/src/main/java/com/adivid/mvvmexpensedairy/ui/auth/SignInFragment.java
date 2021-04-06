package com.adivid.mvvmexpensedairy.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.databinding.FragmentSignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private GoogleSignInClient googleSignInClient;
    private SignInViewModel viewModel;

    @Inject
    public FirebaseAuth firebaseAuth;

    public SignInFragment() {
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSignInBinding.bind(view);

        init();
        observers();
        setUpOnClickListners();
        setUpGoogleSignInClient();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);
    }

    private void observers() {
        viewModel.firebaseUser.observe(getViewLifecycleOwner(), firebaseUser -> {
            Timber.d("photo url : " + firebaseUser.getPhotoUrl());
        });
    }

    private void setUpOnClickListners() {
        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.buttonSignIn.setOnClickListener(v -> {
            showProgressBar(true);
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startForResult.launch(signInIntent);
        });

    }

    private void setUpGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
    }

    private final ActivityResultLauncher<Intent> startForResult  =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    showProgressBar(false);
                    GoogleSignInAccount account =
                            GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
                    firebaseAuthWithGoogle(account.getIdToken());

                }else{
                    showProgressBar(false);
                    Toast.makeText(requireContext(), "Some Error Occurred",
                            Toast.LENGTH_SHORT).show();
                    Timber.d("result not ok");
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        viewModel.firebaseAuthWithGoogle(idToken);
    }

    private void showProgressBar(boolean show) {
        if(show) binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.GONE);
    }
}
