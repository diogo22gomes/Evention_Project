package com.example.evention.ui.components.eventDetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.evention.model.Event

@Composable
fun EventDescription(event: Event){
    Text(
        text = "About Event",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 10.dp)
    )
    Text(
        text = event.description,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(horizontal = 25.dp)
    )
}