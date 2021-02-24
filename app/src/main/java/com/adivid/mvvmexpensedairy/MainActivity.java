package com.adivid.mvvmexpensedairy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.adivid.mvvmexpensedairy.adapter.MenuAdapter;
import com.adivid.mvvmexpensedairy.databinding.ActivityMainBinding;
import com.adivid.mvvmexpensedairy.model.Menu;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private List<Menu> menuList;
    private NavController navController;
    private String lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setUpOnClickListeners();

    }

    private void init() {
        NavHostFragment navFragment =(NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        navController = navFragment.getNavController();
        setUpMenuList();
    }

    private void setUpOnClickListeners() {
        setUpListViewClickListener();

    }

    private void setUpListViewClickListener() {
        binding.menuListView.setOnItemClickListener((parent, view, position, id) -> {
            closeDrawer();
            switch (menuList.get(position).getMenu_name()){
                case "All Transactions":
                    if(navController.getCurrentDestination().getId() != R.id.allTransactionsFragment2){
                        navController.navigate(R.id.action_dashboardFragment_to_allTransactionsFragment2);
                    }
                    break;
                case "Day Report":
                    if(navController.getCurrentDestination().getId() != R.id.dayTransactionsFragment){
                        navController.navigate(R.id.action_dashboardFragment_to_dayTransactionsFragment);
                    }
                    break;
                case "Monthly Report":
                    if(navController.getCurrentDestination().getId() != R.id.monthTransactionFragment){
                        navController.navigate(R.id.action_dashboardFragment_to_monthTransactionFragment);
                    }
                    break;
                case "Categories":
                    Timber.d("Categories");
                    break;
                case "Settings":
                    Timber.d("Settings");
                    break;
                case "Rate App":
                    Timber.d("Rate");
                    break;
            }
        });
    }

    private void setUpMenuList() {
        menuList = new ArrayList<>();

        menuList.add(new Menu("All Transactions", R.drawable.ic_all_transaction_list));
        menuList.add(new Menu("Day Report", R.drawable.ic_day_calendar));
        menuList.add(new Menu("Monthly Report", R.drawable.ic_month_calendar));
        menuList.add(new Menu("Categories", R.drawable.ic_category_add));
        menuList.add(new Menu("Settings", R.drawable.ic_settings));
        menuList.add(new Menu("Rate App", R.drawable.ic_star));

        MenuAdapter menuAdapter = new MenuAdapter(this, R.layout.menu_list_item,
                menuList);

        binding.menuListView.setAdapter(menuAdapter);

        menuAdapter.notifyDataSetChanged();

    }

    public void openDrawer() {
        binding.drawerlayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START);
        }
    }
}