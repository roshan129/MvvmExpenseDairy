package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CashCardChipItems(
    modifier: Modifier,
    chipItemList: List<String>,
    onChipItemSelected: (String) -> Unit
) {

    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var selectedItem by remember {
            mutableStateOf(chipItemList[0])
        }

        LazyRow(
            Modifier.padding(start = 8.dp, end = 8.dp, top = 12.dp),
        ) {

            items(chipItemList) { item ->
                ChipsItems(
                    chipItem = item,
                    selectedItem = selectedItem,
                    onChipItemSelected = { chipItem ->
                        selectedItem = chipItem
                        onChipItemSelected(chipItem)
                    }
                )
            }

        }
    }

}