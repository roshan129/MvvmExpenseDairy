package com.roshanadke.mvvmexpensedairy.ui.others;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.roshanadke.mvvmexpensedairy.R;
import com.roshanadke.mvvmexpensedairy.databinding.FragmentSettingsBinding;
import com.roshanadke.mvvmexpensedairy.utils.SharedPrefManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Inject
    public SharedPrefManager sharedPrefManager;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSettingsBinding.bind(view);

        init();
        setUpOnClickListners();
    }

    private void init() {
        binding.switchDarkMode.setChecked(sharedPrefManager.isNightModeOn());

        binding.switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPrefManager.saveNightMode(true);
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPrefManager.saveNightMode(false);
            }
        });
    }

    private void setUpOnClickListners() {
        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }
}
