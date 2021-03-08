package com.adivid.mvvmexpensedairy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.adivid.mvvmexpensedairy.adapter.MenuAdapter;
import com.adivid.mvvmexpensedairy.databinding.ActivityMainBinding;
import com.adivid.mvvmexpensedairy.model.Menu;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setUpOnClickListeners();
    }

    private void init() {
        NavHostFragment navFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        navController = navFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).
                setOpenableLayout(binding.drawerlayout).build();
        NavigationUI.setupWithNavController(binding.navigationView, navController);

        //setUpMenuList();
    }

    private void setUpOnClickListeners() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

        });

    }

    public void openDrawer() {
        binding.drawerlayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START);
        }
    }
}