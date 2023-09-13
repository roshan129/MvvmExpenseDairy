package com.roshanadke.mvvmexpensedairy.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.roshanadke.mvvmexpensedairy.domain.model.Expense

@Composable
fun DashboardScreen(
    navController: NavController
) {



}


fun getList(): List<Expense> {

    return listOf(
        Expense(
            1,
                System.currentTimeMillis().toString(),
                System.currentTimeMillis().toString(),
            "100",


        ),

    )

}