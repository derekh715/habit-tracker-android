package edu.cuhk.csci3310.ui.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.ui.formUtils.MyTextField

@Composable
fun DebugScreen(viewModel: DebugScreenViewModel = hiltViewModel()) {
    val hAmount = viewModel.habitAmount.collectAsState()
    val gAmount = viewModel.groupAmount.collectAsState()
    val rAmount = viewModel.recordAmount.collectAsState()

    Column {
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
