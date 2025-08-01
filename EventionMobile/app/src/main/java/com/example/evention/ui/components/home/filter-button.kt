package com.example.evention.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.evention.ui.theme.EventionBlue
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun FilterButtonWithDateRange(
    onDateRangeSelected: (startDate: Long?, endDate: Long?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    FilterButton(onClick = { showDatePicker = true })

    if (showDatePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { start, end ->
                if (start != null && end != null) {
                    onDateRangeSelected(start, end)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun FilterButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = EventionBlue,
            contentColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = ButtonDefaults.buttonElevation(4.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Filter",
            modifier = Modifier.size(25.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (startDate: Long?, endDate: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val today = remember { LocalDate.now() }
    val todayInMillis = remember { today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() }

    val dateRangePickerState = rememberDateRangePickerState()

    val startDate = dateRangePickerState.selectedStartDateMillis
    val endDate = dateRangePickerState.selectedEndDateMillis

    val isValidRange = startDate != null && endDate != null &&
            startDate >= todayInMillis && endDate >= todayInMillis

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValidRange) {
                        onDateRangeSelected(startDate, endDate)
                        onDismiss()
                    }
                },
                enabled = isValidRange
            ) {
                Text("Confirm", color = if (isValidRange) EventionBlue else Color.Gray)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        colors = DatePickerDefaults.colors(
            titleContentColor = EventionBlue,
            headlineContentColor = EventionBlue,
            weekdayContentColor = EventionBlue,
            selectedDayContainerColor = EventionBlue,
            selectedDayContentColor = Color.White
        )
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(text = "Select date range")
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

