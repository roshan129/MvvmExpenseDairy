package com.adivid.mvvmexpensedairy.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class SignInRepository {

    private final FirebaseAuth firebaseAuth;

    @Inject
    public SignInRepository(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public Single<FirebaseUser> firebaseAuthWithGoogle(String idToken) {
        Timber.d("inside repository");
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        return Single.create(emitter ->
                firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(new FirebaseAuthWithGoogle(), task -> {
                    if (task.isSuccessful()) {
                        Timber.d("signInWithCredential:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        emitter.onSuccess(user);
                    } else {
                        Timber.d("signInWithCredential:failure%s", task.getException().toString());
                        emitter.onError(task.getException());
                    }
                }));

      /*  return Single.create(emitter -> firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    Timber.d("inside addOnCompleteListener");
                    if(task.isSuccessful()) {
                        Timber.d("signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        emitter.onSuccess(user);
                    }else{
                        Timber.d("error%s", task.getException().toString());
                        emitter.onError(task.getException());
                    }
                }));
*/
    }

    static class FirebaseAuthWithGoogle implements Executor {
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }

}
