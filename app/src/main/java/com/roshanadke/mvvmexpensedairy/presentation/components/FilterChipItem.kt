package com.roshanadke.mvvmexpensedairy.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipsItems(
    chipItem: String,
    selectedItem: String,
    onChipItemSelected: (chipItem: String) -> Unit,
) {

    val chipColor = FilterChipDefaults.filterChipColors(
        containerColor = Color.White,
        labelColor = Color.Black,
        selectedContainerColor = Color.Red,
        selectedLabelColor = Color.White
    )
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        selected = (chipItem == selectedItem),
        onClick = {
            onChipItemSelected(chipItem)
        },
        label = {
            Text(text = chipItem)
        },
        colors = chipColor
    )
}

