package com.example.evention.ui.components.createEvent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng

@Composable
fun LocationSelectorField(
    labelText: String,
    selectedLocation: LatLng?,
    displayAddress: String,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value = displayAddress,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = { Text(labelText, color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Black,
            disabledBorderColor = Color.Black,
        )
    )
}

