package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roshanadke.mvvmexpensedairy.R
import com.roshanadke.mvvmexpensedairy.presentation.ui.light_blue


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