package edu.cuhk.csci3310.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
    val amount = viewModel.amount.collectAsState()
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Debug Section",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        MyTextField(info = amount.value, onValueChange = {
            viewModel.valueChanged(it)
        })
        Button(onClick = {
            viewModel.addHabits()
        }) {
            Text("Add dummy habit(s)")
        }
        Button(onClick = {
            viewModel.addGroups()
        }) {
            Text("Add dummy group(s)")
        }
        Button(onClick = {
            viewModel.addRecords()
        }) {
            Text("Add dummy record(s)")
        }
        Text(text = "Notifications")
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
        Text(text = "Remove")
        Button(onClick = {
            viewModel.removeAllHabits()
        }) {
            Text("Remove All Habits")
        }
        Button(onClick = {
            viewModel.removeAllGroups()
        }) {
            Text("Remove All Groups")
        }
        Button(onClick = {
            viewModel.removeAllRecord()
        }) {
            Text("Remove All Records")
        }

    }
}