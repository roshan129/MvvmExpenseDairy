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
import com.roshanadke.mvvmexpensedairy.presentation.components.ExpenseCardItem
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

@Composable
fun OverViewReportTab(
    onDailyTabClicked: () -> Unit,
    onMonthlyTabClicked: () -> Unit,
    onYearlyTabClicked: () -> Unit
) {

    Text(
        text = "Overview Report",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 24.dp)
    )
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,

        ) {

        val cardColumnPadding = 14.dp



        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    onDailyTabClicked()
                },

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            //border = BorderStroke(width = 2.dp, color = Color.Red)
        ) {
            Column(
                Modifier
                    .padding(cardColumnPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Daily", fontSize = 18.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

            }

        }


        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    onMonthlyTabClicked()
                },

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            //border = BorderStroke(width = 2.dp, color = Color.Red)
        ) {
            Column(
                Modifier
                    .padding(cardColumnPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Monthly", fontSize = 18.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

            }

        }

        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f)
                .clickable {
                    onYearlyTabClicked()
                },

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            //border = BorderStroke(width = 2.dp, color = Color.Red)
        ) {
            Column(
                Modifier
                    .padding(cardColumnPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Yearly", fontSize = 18.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

            }

        }
    }

}

@Composable
fun BalanceTab(expense: Double, income: Double) {

    Column {
        Text(
            text = "This Month", fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 24.dp, top = 8.dp)
        )

        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = light_blue)


        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {

                Row {
                    Icon(
                        painter = painterResource(id = R.drawable.rupee),
                        contentDescription = "balance_icon",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = "Balance", color = Color.White,
                        fontStyle = FontStyle(R.font.quicksand_semibold),
                        fontSize = 24.sp
                    )

                }
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "${stringResource(id = R.string.rupee)}${expense + income}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )

            }


        }
    }


}

@Composable
fun TotalExpenseIncomeLayout(
    expense: Double,
    income: Double
) {

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f),

            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(width = 2.dp, color = Color.Red)
        ) {

            val rupeeSymbol = stringResource(id = R.string.rupee)

            Column(
                Modifier
                    .padding(18.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Expense", fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle(R.font.quicksand_medium)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$rupeeSymbol$expense",
                    color = Color.Red,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(width = 2.dp, color = Color.Green)
        ) {

            val rupeeSymbol = stringResource(id = R.string.rupee)

            Column(
                Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Income", fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle(R.font.quicksand_medium)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$rupeeSymbol$income",
                    color = Color.Green,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

            }
        }

        Spacer(modifier = Modifier.width(12.dp))


    }


}
