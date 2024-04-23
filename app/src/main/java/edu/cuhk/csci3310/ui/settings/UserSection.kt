package edu.cuhk.csci3310.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.di.Settings
import edu.cuhk.csci3310.ui.formUtils.MyCheckbox
import edu.cuhk.csci3310.ui.formUtils.MyTimePicker

@Composable
fun UserSection(
    viewModel: UserSectionViewModel = hiltViewModel()
) {
    val postNotificationInfo = viewModel.postNotification.collectAsState()
    val notifyAt = viewModel.notifyAt.collectAsState()
    val showDebugOptions = viewModel.showDebugOptions.collectAsState()

    Column {
        Text(
            text = "User Section",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        MyCheckbox(
            info = postNotificationInfo.value,
            onSelected = {
                viewModel.setValue(
                    Settings.POST_NOTIFICATIONS,
                    !postNotificationInfo.value.toggled
                )
            })
        MyCheckbox(
            info = showDebugOptions.value,
            onSelected = {
                viewModel.setValue(
                    Settings.SHOW_DEBUG_OPTIONS,
                    !showDebugOptions.value.toggled
                )
            })
        Text(
            text = "Notify at:"
        )
        MyTimePicker(time = notifyAt.value, timeChosen = { time ->
            viewModel.changeNotifyAt(time)
        })
    }
}