package com.adivid.mvvmexpensedairy.ui.categories;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.databinding.FragmentCategoriesBinding;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class CategoriesFragment extends Fragment {

    private FragmentCategoriesBinding binding;

    public CategoriesFragment() {
        super(R.layout.fragment_categories);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCategoriesBinding.bind(view);

        init();
    }

    private void init() {
        binding.buttonEffect.setOnClickListener(v -> {
            Timber.d("clikced");
        });
    }
}
