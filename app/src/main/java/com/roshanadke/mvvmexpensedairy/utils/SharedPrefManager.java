package com.roshanadke.mvvmexpensedairy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_NIGHT_MODE;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_USER_EMAIL;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.KEY_USER_NAME;
import static com.roshanadke.mvvmexpensedairy.utils.Constants.SHARED_PREF_NAME;

public class SharedPrefManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public boolean isNightModeOn() {
        return sharedPreferences.getBoolean(KEY_NIGHT_MODE, false);
    }

    public void saveNightMode(boolean b) {
        editor.putBoolean(KEY_NIGHT_MODE, b).apply();
    }

    public void clearAllPrefs() {
        saveUserName("");
        saveEmail("");
    }

    public void saveUserName(String name) {
        editor.putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public void saveEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }


}
