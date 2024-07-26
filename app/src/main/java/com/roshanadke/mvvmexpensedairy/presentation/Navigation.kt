package com.roshanadke.mvvmexpensedairy.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.presentation.screen.AddExpenseScreen
import com.roshanadke.mvvmexpensedairy.presentation.screen.DashboardScreen
import com.roshanadke.mvvmexpensedairy.presentation.screen.Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.DashboardScreen.route
    ) {

        composable(Screen.DashboardScreen.route) {
            DashboardScreen(
                navController = navController,
                onFloatingActionButtonClicked = {
                    navController.navigate(Screen.AddExpenseScreen.route)
                },
                onExpenseListItemClicked = { expense: Expense ->
                    navController.navigate(Screen.AddExpenseScreen.route)
                })
        }
        composable(Screen.AddExpenseScreen.route) {
            AddExpenseScreen(navController = navController)
        }
    }
}