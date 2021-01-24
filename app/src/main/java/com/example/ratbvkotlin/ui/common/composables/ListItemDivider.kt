package com.example.ratbvkotlin.ui.common.composables

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ListItemDivider(
    index: Int,
    listSize: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray,
    thickness: Dp = 1.dp
) {
    when {
        listSize > 0 && index < listSize - 1 -> {
            Divider(
                modifier,
                color,
                thickness
            )
        }
    }
}