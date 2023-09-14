package com.roshanadke.mvvmexpensedairy.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.roshanadke.mvvmexpensedairy.presentation.components.CashCardChipItems
import com.roshanadke.mvvmexpensedairy.presentation.components.CategoryDropDownItem
import com.roshanadke.mvvmexpensedairy.presentation.components.ExpenseChipType
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddExpenseScreen(
    navController: NavController
) {



    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester),
                onClick = {

                },
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        }
    ) {

        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp),
        ) {

            Text(
                text = "Add Expense",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            val roundedCornerShapeSize = 12.dp

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    value = "14 Sept", onValueChange = {},
                    label = {
                        Text(text = "Date")
                    },
                    shape = RoundedCornerShape(roundedCornerShapeSize),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    value = "16:16", onValueChange = {},
                    label = {
                        Text(text = "Time")
                    },
                    shape = RoundedCornerShape(roundedCornerShapeSize),
                    modifier = Modifier.weight(1f),
                    readOnly = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "", onValueChange = {},
                label = {
                    Text(text = "Amount")
                },
                shape = RoundedCornerShape(roundedCornerShapeSize),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            val chipItemList = listOf("Expense", "Income")
            var selectedExpenseChipItem by remember {
                mutableStateOf(chipItemList[0])
            }

            ExpenseChipType(chipItemList = chipItemList, onChipItemSelected = {
                selectedExpenseChipItem = it
            })

            val items = listOf(
                "Others", "Food and Dining", "Shopping", "Travelling",
                "Entertainment", "Medical"
            )
            var selectedCategoryItem by remember { mutableStateOf(items[0]) }

            CategoryDropDownItem(
                dropDownList = items,
                onDropDownItemSelected = {
                    selectedCategoryItem = it
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = "", onValueChange = {

                },
                label = {
                    Text(text = "Note")
                },
                shape = RoundedCornerShape(roundedCornerShapeSize),
                modifier = Modifier.fillMaxWidth().onFocusEvent {event ->
                    if(event.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }

            )

            Spacer(modifier = Modifier.height(12.dp))

            val paymentItemList = listOf("Cash", "Card")
            var selectedPaymentTypeChip by remember {
                mutableStateOf(paymentItemList[0])
            }
            CashCardChipItems(
                modifier = Modifier.padding(start = 4.dp),
                chipItemList = paymentItemList,
                onChipItemSelected = {
                    selectedPaymentTypeChip = it
                }
            )


        }
    }


}


