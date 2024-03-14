package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(datePickerState: DatePickerState) {
    var opened by remember {
        mutableStateOf(false)
    }
    if (opened) {
        DatePickerDialog(onDismissRequest = { opened = false }, confirmButton = {
            TextButton(onClick = { opened = false }) {
                Text("Confirm")
            }
        }, dismissButton = {
            TextButton(onClick = { opened = false }) {
                Text("Dismiss")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
    Button(onClick = { opened = true }) {
        Text("Choose Date ${datePickerState.selectedDateMillis}")
    }
}
