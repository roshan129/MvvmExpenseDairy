package com.roshanadke.mvvmexpensedairy.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.presentation.components.BalanceTab
import com.roshanadke.mvvmexpensedairy.presentation.components.ExpenseCardItem
import com.roshanadke.mvvmexpensedairy.presentation.components.OverViewReportTab
import com.roshanadke.mvvmexpensedairy.presentation.components.TotalExpenseIncomeLayout
import com.roshanadke.mvvmexpensedairy.presentation.viewmodel.DashboardViewModel
import kotlin.math.exp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onFloatingActionButtonClicked: () -> Unit,
    onExpenseListItemClicked: (expense: Expense) -> Unit
) {

    val list = dashboardViewModel.expenseList.toList()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onFloatingActionButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
        content = {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    Spacer(modifier = Modifier.height(50.dp))

                    OverViewReportTab(
                        onDailyTabClicked = {
                            Log.d("TAG", "DashboardScreen: on daily tab clicked")
                        },
                        onMonthlyTabClicked = {

                        },
                        onYearlyTabClicked = {

                        }
                    )

                    BalanceTab(
                        dashboardViewModel.expenseCount.value,
                        dashboardViewModel.incomeCount.value,
                    )

                    TotalExpenseIncomeLayout(
                        dashboardViewModel.expenseCount.value,
                        dashboardViewModel.incomeCount.value,
                    )

                    Text(text = "Latest Transaction", modifier = Modifier.padding(24.dp))

                }
                items(list) { expense ->
                    ExpenseCardItem(
                        expense,
                        onExpenseListItemClicked = {
                            onExpenseListItemClicked(expense)
                        }
                    )
                }
            }


        }
    )

}


