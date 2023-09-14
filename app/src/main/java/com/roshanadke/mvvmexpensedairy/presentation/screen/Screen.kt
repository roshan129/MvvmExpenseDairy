package com.roshanadke.mvvmexpensedairy.presentation.screen

sealed class Screen(val route: String) {

    object DashboardScreen: Screen("dashboard")
    object AddExpenseScreen: Screen("add_expense")

}