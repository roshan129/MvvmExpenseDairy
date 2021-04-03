package com.adivid.mvvmexpensedairy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.adivid.mvvmexpensedairy.adapter.MenuAdapter;
import com.adivid.mvvmexpensedairy.data.db.ExpenseDao;
import com.adivid.mvvmexpensedairy.data.db.ExpenseDiaryDatabase;
import com.adivid.mvvmexpensedairy.data.db.ExpenseEntity;
import com.adivid.mvvmexpensedairy.databinding.ActivityMainBinding;
import com.adivid.mvvmexpensedairy.model.Menu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Inject
    public ExpenseDao expenseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MvvmExpenseDairy);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        Date date = new Date();
        date.setTime(1617125075);
        ExpenseEntity expenseEntity = new ExpenseEntity(
                date, "11:11", "55.0", "Expense", "Others", "Test Dummy One",
                "Cash", "1617124057887"
        );
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            expenseDao.insertTransaction(expenseEntity);

        });

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
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            Timber.d("clicked");
            if(item.getItemId() == R.id.item_about){
                showAboutAlertDialog();
            }
            NavigationUI.onNavDestinationSelected(item,navController);
            closeDrawer();
            return true;
        });

    }

    private void showAboutAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xpense Dairy")
                .setMessage("Version: 1.0\nVersion Code: 1")
                .setPositiveButton("Ok", (dialog, which) -> {
                   dialog.dismiss();
                });
        builder.show();
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