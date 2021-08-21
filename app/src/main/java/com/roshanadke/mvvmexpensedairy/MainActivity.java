package com.roshanadke.mvvmexpensedairy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.roshanadke.mvvmexpensedairy.data.db.ExpenseDao;
import com.roshanadke.mvvmexpensedairy.data.db.ExpenseEntity;
import com.roshanadke.mvvmexpensedairy.databinding.ActivityMainBinding;
import com.roshanadke.mvvmexpensedairy.utils.SharedPrefManager;

import java.util.Date;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private View headerView;
    private TextView textViewMenuHeaderText;
    private Button buttonMenuHeaderButton;

    @Inject
    public ExpenseDao expenseDao;

    @Inject
    public SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MvvmExpenseDairy);
        setUpUiMode();
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

        headerView = binding.navigationView.getHeaderView(0);
        textViewMenuHeaderText = headerView.findViewById(R.id.textViewDrawerLayout);
        buttonMenuHeaderButton = headerView.findViewById(R.id.buttonDrawerLayout);

        //setUpMenuList();
    }

    private void setUpOnClickListeners() {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

        });
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            Timber.d("clicked");
            if (item.getItemId() == R.id.item_about) {
                showAboutAlertDialog();
            }
            NavigationUI.onNavDestinationSelected(item, navController);
            closeDrawer();
            return true;
        });

        buttonMenuHeaderButton.setOnClickListener(v -> {
            closeDrawer();
            navController.navigate(R.id.action_dashboardFragment_to_signInFragment);
        });
    }

    public void setUpDrawerLayoutHeaders() {
        String email = sharedPrefManager.getEmail();
        if (email != null && !email.isEmpty()) {
                textViewMenuHeaderText.setText(email);
                buttonMenuHeaderButton.setText("Sign Out");
        }else{
            textViewMenuHeaderText.setText("Hello, Guest User");
            buttonMenuHeaderButton.setText("Sign In");
        }
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

    private void setUpUiMode() {
        if (sharedPrefManager.isNightModeOn()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (binding.drawerlayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerlayout.closeDrawer(GravityCompat.START);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpDrawerLayoutHeaders();
    }
}