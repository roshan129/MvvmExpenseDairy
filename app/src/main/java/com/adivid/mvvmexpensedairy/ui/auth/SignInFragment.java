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
import com.adivid.mvvmexpensedairy.utils.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private GoogleSignInClient googleSignInClient;
    private SignInViewModel viewModel;

    @Inject
    public SharedPrefManager sharedPrefManager;

    @Inject
    public FirebaseAuth firebaseAuth;

    private GoogleSignInOptions googleSignInOptions;

    public SignInFragment() {
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSignInBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();
        setUpGoogleSignInClient();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            showUserData(user);
        }
    }

    private void observers() {
        viewModel.firebaseUser.observe(getViewLifecycleOwner(), firebaseUserResource -> {
            switch (firebaseUserResource.status) {
                case SUCCESS:
                    showProgressBar(false);
                    FirebaseUser user = firebaseUserResource.data;
                    if (user != null) {
                        sharedPrefManager.saveUserName(user.getDisplayName());
                        sharedPrefManager.saveEmail(user.getEmail());
                        showUserData(user);
                    }else{
                        showToast("Some Error Occurred");
                    }
                    break;
                case LOADING:
                    showProgressBar(true);
                    break;
                case ERROR:
                    showProgressBar(false);
                    showToast("Some Error Occurred");
                    break;
            }


        });
    }

    private void showUserData(FirebaseUser user) {
        binding.tvName.setText(user.getDisplayName());
        binding.tvEmail.setText(user.getEmail());
        binding.tvName.setVisibility(View.VISIBLE);
        binding.tvEmail.setVisibility(View.VISIBLE);
        binding.buttonSignIn.setText(getString(R.string.sign_out));
    }

    private void setUpOnClickListeners() {
        binding.ivBack.setOnClickListener(v -> requireActivity().onBackPressed());

        binding.buttonSignIn.setOnClickListener(v -> {

            if (binding.buttonSignIn.getText().equals(getString(R.string.sign_in))) {
                showProgressBar(true);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startForResult.launch(signInIntent);
            } else {
                GoogleSignInClient signInClient = GoogleSignIn.getClient(requireContext(),
                        googleSignInOptions);
                firebaseAuth.signOut();
                sharedPrefManager.clearAllPrefs();
                signInClient.signOut().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Timber.d("google sign out successful");
                        showToast("Successfully Logged Out");
                        binding.buttonSignIn.setText(getString(R.string.sign_in));
                        binding.tvName.setVisibility(View.GONE);
                        binding.tvEmail.setVisibility(View.GONE);
                    } else {
                        Timber.d("unsuccessfull");
                    }
                });

            }

        });

    }

    private void setUpGoogleSignInClient() {
        googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions);
    }

    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                showProgressBar(false);
                if (result.getResultCode() == Activity.RESULT_OK) {
                    GoogleSignInAccount account =
                            GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult();
                    firebaseAuthWithGoogle(account.getIdToken());

                } else {
                    showToast("Some Error Occurred");
                    Timber.d("result not ok");
                }
            });

    private void firebaseAuthWithGoogle(String idToken) {
        viewModel.firebaseAuthWithGoogle(idToken);
    }

    private void showProgressBar(boolean show) {
        if (show) binding.progressBar.setVisibility(View.VISIBLE);
        else binding.progressBar.setVisibility(View.GONE);
    }

    private void showToast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
