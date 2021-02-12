package com.adivid.mvvmexpensedairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setUpOnClickListeners();


    }

    private void init() {

        setUpMenuList();
    }

    private void setUpOnClickListeners() {
        setUpListViewClickListener();


    }

    private void setUpListViewClickListener() {
        binding.menuListView.setOnItemClickListener((parent, view, position, id) -> {
            switch (menuList.get(position).getMenu_name()){
                case "All Transactions":
                    Timber.d("All");
                    break;
                case "Day List":
                    Timber.d("Day");
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
        menuList.add(new Menu("Day List", R.drawable.ic_day_calendar));
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
}