package com.adivid.mvvmexpensedairy.ui.custom_view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.databinding.FragmentCustomViewBinding;
import com.adivid.mvvmexpensedairy.utils.Utils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomViewFragment extends Fragment {

    private FragmentCustomViewBinding binding;
    private CustomViewViewModel viewModel;

    public CustomViewFragment() {
        super(R.layout.fragment_custom_view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCustomViewBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(CustomViewViewModel.class);

        String dateFirst = "01 Mar, 2021";
        String dateLast = "31 Mar, 2021";
        viewModel.getCustomList(Utils.convertToStoringDate(dateFirst),
                Utils.convertToStoringDate(dateLast), "", "");

    }

    private void observers() {

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
