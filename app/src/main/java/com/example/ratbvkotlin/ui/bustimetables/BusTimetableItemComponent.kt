package com.example.ratbvkotlin.ui.bustimetables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ratbvkotlin.ui.resources.typography

@Composable
fun BusTimetableItemComponent(
    hourValue: String,
    minuteValues: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 4.dp,
                bottom = 4.dp
            )
    ) {
        Text(
            text = hourValue,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier
                .padding(4.dp)
                .weight(0.25f)
        )
        Text(
            text = minuteValues,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Normal,
            style = typography.h6,
            modifier = Modifier
                .padding(4.dp)
                .weight(0.75f)
        )
    }
}