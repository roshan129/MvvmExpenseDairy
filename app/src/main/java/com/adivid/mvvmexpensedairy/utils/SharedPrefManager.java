package com.adivid.mvvmexpensedairy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.adivid.mvvmexpensedairy.utils.Constants.KEY_NIGHT_MODE;
import static com.adivid.mvvmexpensedairy.utils.Constants.SHARED_PREF_NAME;

public class SharedPrefManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
        editor.clear().apply();
    }

}
