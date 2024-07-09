package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
