package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CategoryDropDownItem(
    modifier: Modifier = Modifier,
    dropDownList: List<String>,
    onDropDownItemSelected: (String) -> Unit
) {


    Row(
        modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var expanded by remember { mutableStateOf(false) }
        var selectedItem by remember { mutableStateOf("Others") }

        Text(text = "Category: ", modifier = Modifier.padding(start = 12.dp))
        Spacer(modifier = Modifier.width(24.dp))

        Column {
            DropdownMenu(expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }) {

                dropDownList.forEach { category ->
                    DropdownMenuItem(text = {
                        Text(text = category)
                    }, onClick = {
                        selectedItem = category
                        expanded = false
                        onDropDownItemSelected(category)
                    })
                }

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Text(
                    text = selectedItem,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp
                )
            }
        }

    }


}