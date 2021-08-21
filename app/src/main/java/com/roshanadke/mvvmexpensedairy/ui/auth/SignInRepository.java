package com.roshanadke.mvvmexpensedairy.ui.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
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
