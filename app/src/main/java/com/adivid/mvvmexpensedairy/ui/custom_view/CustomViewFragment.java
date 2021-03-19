package com.adivid.mvvmexpensedairy.ui.custom_view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.databinding.FragmentCustomViewBinding;

public class CustomViewFragment extends Fragment {

    private FragmentCustomViewBinding binding;

    public CustomViewFragment() {
        super(R.layout.fragment_custom_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCustomViewBinding.bind(view);

        init();
        setUpOnClickListeners();
    }

    private void init() {

    }

    private void setUpOnClickListeners() {
        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.ivFilter.setOnClickListener(v -> {
            FilterBottomSheetFragment fragment = new FilterBottomSheetFragment();
            fragment.show(getChildFragmentManager(), "FilterBottomSheetFragment");
            fragment.setCancelable(true);
        });

    }
}
