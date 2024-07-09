package com.roshanadke.mvvmexpensedairy.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.roshanadke.mvvmexpensedairy.R
import com.roshanadke.mvvmexpensedairy.presentation.components.BalanceTab
import com.roshanadke.mvvmexpensedairy.presentation.components.ExpenseCardItem
import com.roshanadke.mvvmexpensedairy.presentation.components.OverViewReportTab
import com.roshanadke.mvvmexpensedairy.presentation.components.TotalExpenseIncomeLayout
import com.roshanadke.mvvmexpensedairy.presentation.ui.light_blue
import com.roshanadke.mvvmexpensedairy.presentation.viewmodel.DashboardViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardScreen(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onFloatingActionButtonClicked: () -> Unit
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
                        expense
                    )
                }
            }


        }
    )

}


