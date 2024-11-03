package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun DateField(
    modifier: Modifier = Modifier,
    selectedDate: String,
    onValueChange: (String) -> Unit,
    label: String,
    shape: RoundedCornerShape,
    readOnly: Boolean,
    isDatePickerVisible: (Boolean) -> Unit
) {

    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = selectedDate ?: "", onValueChange = {},
        label = {
            Text(text = "Date")
        },

        shape = shape,
        modifier = modifier
            .onFocusChanged {
                if (it.isFocused) {
                    isDatePickerVisible(true)
                    focusManager.clearFocus()
                }

            },
        readOnly = true,
    )
}