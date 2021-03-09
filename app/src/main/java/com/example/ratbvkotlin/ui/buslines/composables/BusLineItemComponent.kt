package com.example.ratbvkotlin.ui.buslines.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
fun BusLineItemComponent(
    name: String,
    route: String,
    onItemClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .padding(
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            style = typography.h5,
            modifier = Modifier
                .padding(4.dp)
        )
        Text(
            text = route,
            textAlign = TextAlign.Start,
            style = typography.subtitle1,
            modifier = Modifier
                .padding(
                    top = 4.dp,
                    bottom = 0.dp,
                    start = 4.dp,
                    end = 4.dp
                )
        )
    }
}

//@Preview
//@Composable
//fun ComposablePreview() {
//    BusLineItemComponent("Test", "Test") { }
//}