package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roshanadke.mvvmexpensedairy.R


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