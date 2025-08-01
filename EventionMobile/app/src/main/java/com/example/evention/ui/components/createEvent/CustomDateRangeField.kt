package com.example.evention.ui.components.createEvent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun CustomDateRangeTextField(
    labelText: String,
    startDate: Date?,
    endDate: Date?,
    formatter: SimpleDateFormat,
    onDateRangeSelected: (Date, Date) -> Unit
) {
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val calendar = remember { Calendar.getInstance() }
    var tempStartCalendar = remember { Calendar.getInstance() }
    var tempEndCalendar = remember { Calendar.getInstance() }

    val displayText = if (startDate != null && endDate != null) {
        "${formatter.format(startDate)} - ${formatter.format(endDate)}"
    } else {
        ""
    }

    OutlinedTextField(
        value = displayText,
        onValueChange = {},
        readOnly = true,
        enabled = false,
        label = { Text(labelText, color = Color.Black) },
        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                tempStartCalendar = Calendar.getInstance()
                tempEndCalendar = Calendar.getInstance()
                showStartDatePicker = true
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            disabledTextColor = Color.Black,
            disabledLabelColor = Color.Black,
            disabledBorderColor = Color.Black,
        )
    )

    val context = LocalContext.current

    // Start Date Picker
    if (showStartDatePicker) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                tempStartCalendar.set(year, month, dayOfMonth)
                showStartDatePicker = false
                showStartTimePicker = true
            },
            tempStartCalendar.get(Calendar.YEAR),
            tempStartCalendar.get(Calendar.MONTH),
            tempStartCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    if (showStartTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                tempStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                tempStartCalendar.set(Calendar.MINUTE, minute)
                tempStartCalendar.set(Calendar.SECOND, 0)
                tempStartCalendar.set(Calendar.MILLISECOND, 0)
                showStartTimePicker = false
                showEndDatePicker = true
            },
            tempStartCalendar.get(Calendar.HOUR_OF_DAY),
            tempStartCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    if (showEndDatePicker) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                tempEndCalendar.set(year, month, dayOfMonth)
                showEndDatePicker = false
                showEndTimePicker = true
            },
            tempEndCalendar.get(Calendar.YEAR),
            tempEndCalendar.get(Calendar.MONTH),
            tempEndCalendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = tempStartCalendar.timeInMillis

        datePickerDialog.show()
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                tempEndCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                tempEndCalendar.set(Calendar.MINUTE, minute)
                tempEndCalendar.set(Calendar.SECOND, 0)
                tempEndCalendar.set(Calendar.MILLISECOND, 0)
                showEndTimePicker = false

                onDateRangeSelected(tempStartCalendar.time, tempEndCalendar.time)
            },
            tempEndCalendar.get(Calendar.HOUR_OF_DAY),
            tempEndCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}






