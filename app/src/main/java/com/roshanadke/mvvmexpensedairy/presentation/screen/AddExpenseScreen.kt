package com.roshanadke.mvvmexpensedairy.presentation.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.roshanadke.mvvmexpensedairy.R
import com.roshanadke.mvvmexpensedairy.domain.model.TransactionType
import com.roshanadke.mvvmexpensedairy.presentation.components.CashCardChipItems
import com.roshanadke.mvvmexpensedairy.presentation.components.CategoryDropDownItem
import com.roshanadke.mvvmexpensedairy.presentation.components.ExpenseChipType
import com.roshanadke.mvvmexpensedairy.presentation.viewmodel.AddExpenseViewModel
import com.roshanadke.mvvmexpensedairy.utils.convertDateStringToMillis
import com.roshanadke.mvvmexpensedairy.utils.convertMillisToDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddExpenseScreen(
    navController: NavController,
    addExpenseViewModel: AddExpenseViewModel = hiltViewModel()
) {

    val selectedDate = addExpenseViewModel.selectedDate.value
    val selectedTime = addExpenseViewModel.selectedTime.value
    val selectedAmount = addExpenseViewModel.selectedDate.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add Expense")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        Log.d("TAG", "AddExpenseScreen: clicked")
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addExpenseViewModel.insertTransaction {
                        navController.popBackStack()
                    }
                },
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
            }
        }

    ) { paddingValues ->

        var isDatePickerVisible by remember { mutableStateOf(false) }
        var isTimePickerVisible by remember { mutableStateOf(false) }

        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 12.dp, top = paddingValues.calculateTopPadding()),
        ) {


            val roundedCornerShapeSize = 12.dp

            //val selectedDate = addExpenseViewModel.selectedDate.value

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = selectedDate ?: "", onValueChange = {},
                    label = {
                        Text(text = "Date")
                    },

                    shape = RoundedCornerShape(roundedCornerShapeSize),
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            if (it.isFocused) {
                                isDatePickerVisible = true
                                focusManager.clearFocus()
                            }

                        },
                    readOnly = true,

                    )
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    value = "16:16", onValueChange = {},
                    label = {
                        Text(text = "Time")
                    },
                    shape = RoundedCornerShape(roundedCornerShapeSize),
                    readOnly = true,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            if (it.isFocused) {
                                isTimePickerVisible = true
                                focusManager.clearFocus()
                            }
                        },

                    )
            }

            Spacer(modifier = Modifier.height(12.dp))

            var amount = remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                value = amount.value, onValueChange = {
                    amount.value = it
                    addExpenseViewModel.setSelectedAmount(it)
                },
                label = {
                    Text(text = "Amount")
                },
                shape = RoundedCornerShape(roundedCornerShapeSize),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = addExpenseViewModel.amountError.value.isNotEmpty(),
                supportingText = {
                    Text(text = addExpenseViewModel.amountError.value, color = Color.Red)
                }
            )

            val chipItemList = listOf("Expense", "Income")
            var selectedExpenseChipItem by remember {
                mutableStateOf(chipItemList[0])
            }

            ExpenseChipType(chipItemList = chipItemList, onChipItemSelected = {
                selectedExpenseChipItem = it
                addExpenseViewModel.setSelectedTransactionType(TransactionType.getTransactionType(it))

            })

            val expenseList = stringArrayResource(id = R.array.category_arr_exp).toList()
            val incomeList = stringArrayResource(id = R.array.category_arr_inc).toList()

            val categoryItems by remember(selectedExpenseChipItem) {
                derivedStateOf {
                    if (selectedExpenseChipItem == TransactionType.Expense.type) expenseList
                    else incomeList
                }
            }

            var selectedCategoryItem by remember { mutableStateOf(categoryItems[0]) }

            CategoryDropDownItem(
                dropDownList = categoryItems,
                onDropDownItemSelected = {
                    selectedCategoryItem = it
                    addExpenseViewModel.setSelectedCategory(it)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            var transactionNote by remember {
                mutableStateOf("")
            }

            OutlinedTextField(
                value = transactionNote, onValueChange = {
                    transactionNote = it
                    addExpenseViewModel.setTransactionNote(it)
                },
                label = {
                    Text(text = "Note")
                },
                shape = RoundedCornerShape(roundedCornerShapeSize),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

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
                    addExpenseViewModel.setPaymentType(it)
                }
            )


        }


        if (isDatePickerVisible) {
            MyDatePickerDialog(
                selectedDate = selectedDate ?: "",
                onDateSelected = {
                    Log.d("TAG", "AddExpenseScreen: date: $it ")
                    addExpenseViewModel.setSelectedDate(it)
                }, onDismiss = {
                    isDatePickerVisible = false
                })
        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= System.currentTimeMillis()
            }
        },
        initialSelectedDateMillis = convertDateStringToMillis(selectedDate)
            ?: System.currentTimeMillis()
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState

        )
    }
}
