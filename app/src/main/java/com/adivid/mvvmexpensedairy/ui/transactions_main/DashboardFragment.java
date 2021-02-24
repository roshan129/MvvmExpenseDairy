package com.adivid.mvvmexpensedairy.ui.transactions_main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adivid.mvvmexpensedairy.MainActivity;
import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.databinding.FragmentDashboardBinding;
import com.adivid.mvvmexpensedairy.domain.Expense;
import com.adivid.mvvmexpensedairy.domain.mapper.ExpenseEntityMapper;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.TIME_INTERVAL;

@AndroidEntryPoint
public class DashboardFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {

    private FragmentDashboardBinding binding;
    private NavController navController;
    private DashboardViewModel viewModel;
    private List<Expense> expenseList;
    private MainListAdapter adapter;
    private long backPressed = 0;

    public DashboardFragment() {
        super(R.layout.fragment_dashboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashboardBinding.bind(view);

        init();
        observers();
        setUpOnClickListeners();

    }

    private void init() {
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        expenseList = new ArrayList<>();

        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);

    }

    private void observers() {
        viewModel.recentAllTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            adapter.submitList(expenseEntities);
            adapter.notifyDataSetChanged();
        });
    }

    private void setUpOnClickListeners() {
        binding.fabAdd.setOnClickListener(v -> {
            navController.navigate(R.id.action_dashboardFragment_to_addTransactionFragment);
        });

        binding.ivMenu.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openDrawer();
        });
    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Timber.d("pos: %s", position);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    };

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void onBackStackChanged() {
        if(backPressed + TIME_INTERVAL > System.currentTimeMillis()){
            requireActivity().finish();
        }else{
            Toast.makeText(requireContext(), "Press back again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        backPressed = System.currentTimeMillis();
    }
}
