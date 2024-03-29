package com.roshanadke.mvvmexpensedairy.ui.auth;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_DOWNLOAD_UNIQUE_WORK;

import android.app.Activity;
import android.app.AlertDialog;
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
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.roshanadke.mvvmexpensedairy.R;
import com.roshanadke.mvvmexpensedairy.databinding.FragmentSignInBinding;
import com.roshanadke.mvvmexpensedairy.utils.DownloadDataWorker;
import com.roshanadke.mvvmexpensedairy.utils.SharedPrefManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
                        fetchDataFromFirebase();
                    } else {
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

    private void fetchDataFromFirebase() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DownloadDataWorker.class)
                .setConstraints(constraints)
                .addTag(KEY_DOWNLOAD_UNIQUE_WORK)
                .build();
        WorkManager.getInstance(requireContext()).enqueueUniqueWork(KEY_DOWNLOAD_UNIQUE_WORK,
                ExistingWorkPolicy.KEEP, request);
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

            if (binding.buttonSignIn.getText().toString().equalsIgnoreCase(getString(R.string.sign_in))) {
                showProgressBar(true);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startForResult.launch(signInIntent);
            } else {
                showAlertDialogForLogOut();
            }

        });

    }

    private void showAlertDialogForLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirm")
                .setMessage("Are you sure want to log out?")
                .setPositiveButton("Logout", (dialog, which) -> {
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
                }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void setUpGoogleSignInClient() {
        googleSignInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        //.requestIdToken("786240409527-vehdu0oqpbaocu91pigb7251oeu23le2.apps.googleusercontent.com")
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

                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    Timber.d("Canceled");
                } else {
                    showToast("Some Error Occurred");
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
