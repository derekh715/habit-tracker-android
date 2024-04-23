package edu.cuhk.csci3310.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.ui.formUtils.MyTextField

@Composable
fun DebugSection(viewModel: DebugSectionViewModel = hiltViewModel()) {
    if (!viewModel.showDebugOptions.collectAsState().value) {
        return Column {

        }
    }

    val hAmount = viewModel.habitAmount.collectAsState()
    val gAmount = viewModel.groupAmount.collectAsState()
    val rAmount = viewModel.recordAmount.collectAsState()

    Column {
        Text(
            text = "Debug Section",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        MyTextField(info = hAmount.value, onValueChange = {
            viewModel.valueChanged(it, TextInputEnum.Habit)
        })
        Button(onClick = {
            viewModel.addHabits()
        }) {
            Text("Add dummy habit(s)")
        }
        MyTextField(info = gAmount.value, onValueChange = {
            viewModel.valueChanged(it, TextInputEnum.Group)
        })
        Button(onClick = {
            viewModel.addGroups()
        }) {
            Text("Add dummy group(s)")
        }
        MyTextField(info = rAmount.value, onValueChange = {
            viewModel.valueChanged(it, TextInputEnum.Record)
        })
        Button(onClick = {
            viewModel.addRecords()
        }) {
            Text("Add dummy record(s)")
        }
        Button(onClick = {
            viewModel.showTestNotification()
        }) {
            Text("Show Test Notification")
        }
        Button(onClick = {
            viewModel.showDailyNotification()
        }) {
            Text("Show Daily Notification")
        }
        Button(onClick = {
            viewModel.scheduleNotification()
        }) {
            Text("Schedule Test Notification")
        }
    }
}