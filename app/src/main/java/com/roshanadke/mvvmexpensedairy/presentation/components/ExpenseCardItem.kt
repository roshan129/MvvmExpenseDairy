package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roshanadke.mvvmexpensedairy.R
import com.roshanadke.mvvmexpensedairy.domain.model.CategoryType
import com.roshanadke.mvvmexpensedairy.domain.model.Expense
import com.roshanadke.mvvmexpensedairy.domain.model.TransactionType
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseCardItem(
    expense: Expense,
    onExpenseListItemClicked: (expense: Expense) -> Unit
) {
    val cardPadding = 16.dp
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = cardPadding, end = cardPadding, bottom = cardPadding),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
          onExpenseListItemClicked(expense)
        }
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Card(
                    modifier = Modifier.padding(6.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {

                    Image(
                        painter = painterResource(id = getCategoryIcon(expense.transactionCategory)),
                        contentDescription = "Share",
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }

                Column(
                    Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(
                        text = expense.note,
                        fontStyle = FontStyle(R.font.quicksand_medium),
                        fontSize = 14.sp
                    )
                    Text(
                        text = expense.date ?: "",
                        fontStyle = FontStyle(R.font.quicksand_regular),
                        fontSize = 12.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "₹ ${expense.amount}", modifier = Modifier.padding(end = 8.dp),
                        fontSize = 16.sp,
                        color = if (expense.transactionType == TransactionType.Expense) {
                            colorResource(id = R.color.text_expense_color)
                        } else {
                            colorResource(id = R.color.text_income_color)
                        }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_cash),
                        contentDescription = "cash_icon",
                    )
                }

            }

        }


    }

}

fun getCategoryIcon(category: String): Int {
    return when (category) {
        CategoryType.OTHERS.displayName -> R.drawable.ic_others
        CategoryType.FOOD_AND_DINING.displayName -> R.drawable.ic_food_and_dining
        CategoryType.SHOPPING.displayName -> R.drawable.ic_shopping_cart
        CategoryType.TRAVELLING.displayName -> R.drawable.ic_travel
        CategoryType.ENTERTAINMENT.displayName -> R.drawable.ic_entertainment
        CategoryType.MEDICAL.displayName -> R.drawable.ic_medical
        CategoryType.PERSONAL_CARE.displayName -> R.drawable.ic_personal_care
        CategoryType.EDUCATION.displayName -> R.drawable.ic_education
        CategoryType.BILLS_AND_UTILITIES.displayName -> R.drawable.ic_bills
        CategoryType.INVESTMENTS.displayName -> R.drawable.ic_investment
        CategoryType.RENT.displayName -> R.drawable.ic_rent
        CategoryType.TAXES.displayName -> R.drawable.ic_taxes
        CategoryType.INSURANCE.displayName -> R.drawable.ic_insurance
        CategoryType.GIFTS_AND_DONATIONS.displayName -> R.drawable.ic_gifts
        CategoryType.SALARY.displayName -> R.drawable.ic_salary
        CategoryType.BONUS.displayName -> R.drawable.ic_bonus
        CategoryType.SAVINGS.displayName -> R.drawable.ic_savings
        CategoryType.DEPOSITS.displayName -> R.drawable.ic_deposits
        else -> R.drawable.ic_others
    }
}


@Preview
@Composable
fun ExpenseCardItemPreview() {
    ExpenseCardItem(
        expense = Expense(
            id = 6804,
            date = "21 Sep, 2023",
            time = "nulla",
            amount = "15000",
            transactionType = TransactionType.Expense,
            transactionCategory = "mentitum",
            note = "dinner and clothes",
            paymentType = "suspendisse"
        ),
        onExpenseListItemClicked = {}
    )
}
