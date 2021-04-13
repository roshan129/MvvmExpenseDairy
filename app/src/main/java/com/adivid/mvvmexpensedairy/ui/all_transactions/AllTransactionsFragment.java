package com.adivid.mvvmexpensedairy.ui.all_transactions;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adivid.mvvmexpensedairy.R;
import com.adivid.mvvmexpensedairy.adapter.MainListAdapter;
import com.adivid.mvvmexpensedairy.adapter.interfaces.OnItemClickListener;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.FragmentAllTransactionsBinding;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

import static com.adivid.mvvmexpensedairy.utils.Constants.EXPENSE_BUNDLE_KEY;

@AndroidEntryPoint
public class AllTransactionsFragment extends Fragment implements
        FragmentManager.OnBackStackChangedListener {

    private FragmentAllTransactionsBinding binding;
    private NavController navController;
    private AllTransactionsViewModel viewModel;
    private MainListAdapter adapter;

    private LinearLayoutManager linearLayoutManager;
    private boolean isScrolling, lockScrolling;
    private int currentItems, scrolledOutItems, totalItems;
    private int counter = 0;
    private List<ExpenseEntity> expenseEntityList;

    public AllTransactionsFragment() {
        super(R.layout.fragment_all_transactions);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAllTransactionsBinding.bind(view);

        init();
        observers();
        setOnItemClickListener();
        viewModel.getAllTransactions(counter);
    }

    private void init() {
        Timber.d("inside all transactions");
        navController = NavHostFragment.findNavController(this);
        viewModel = new ViewModelProvider(this).get(AllTransactionsViewModel.class);

        linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MainListAdapter(recyclerViewClickListener);
        binding.recyclerView.setAdapter(adapter);

        expenseEntityList = new ArrayList<>();

    }

    private void setOnItemClickListener() {
        binding.ivBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrolledOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrolledOutItems == totalItems) && !lockScrolling) {
                    //binding.pro.setVisibility(View.VISIBLE);
                    isScrolling = false;
                    counter++;
                    String offset = counter + "0";
                    Timber.d("counter: " + counter);
                    Timber.d("offse: " + offset);
                    viewModel.getAllTransactions(Integer.parseInt(offset));
                }
            }
        });
    }

    private void observers() {
        viewModel.allTransactions.observe(getViewLifecycleOwner(), expenseEntities -> {
            if(!expenseEntities.isEmpty()) {
                expenseEntityList.addAll(expenseEntities);
                adapter.submitList(expenseEntityList);
            } else {
                lockScrolling = true;
            }
        });

    }

    private final OnItemClickListener recyclerViewClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Timber.d("list clicked" + position);
            ExpenseEntity expenseEntity = expenseEntityList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EXPENSE_BUNDLE_KEY, expenseEntity);
            navController.navigate(R.id.action_allTransactionsFragment2_to_addTransactionFragment,
                    bundle);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    };

    @Override
    public void onBackStackChanged() {
        navController.navigate(R.id.action_allTransactionsFragment2_to_dashboardFragment);
    }
}
