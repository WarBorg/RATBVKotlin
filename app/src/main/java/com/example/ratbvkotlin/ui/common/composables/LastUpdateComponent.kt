package com.example.ratbvkotlin.ui.common.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.ratbvkotlin.R
import com.example.ratbvkotlin.ui.resources.typography

@Composable
fun LastUpdateComposable(
    lastUpdateDate: String,
    modifier: Modifier
) {
    Text(
        text = "${stringResource(id = R.string.last_update_simple)} $lastUpdateDate",
        fontWeight = FontWeight.Normal,
        style = typography.h6,
        modifier = modifier
    )
}