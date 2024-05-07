package edu.cuhk.csci3310.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.ui.formUtils.MyCheckbox
import edu.cuhk.csci3310.ui.formUtils.MyTextField
import edu.cuhk.csci3310.ui.formUtils.MyTimePicker
import edu.cuhk.csci3310.ui.utils.CommonUiEvent

@Composable
fun UserSection(
    viewModel: UserSectionViewModel = hiltViewModel()
) {
    val postNotificationInfo = viewModel.postNotification.collectAsState()
    val notifyAt = viewModel.notifyAt.collectAsState()
    val showDebugOptions = viewModel.showDebugOptions.collectAsState()
    val showRecordsUntil = viewModel.showRecordsUntil.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect { event ->
            when (event) {
                is CommonUiEvent.ShowToast -> {
                    Toast.makeText(context, event.content, Toast.LENGTH_SHORT).show()
                }
            }
        }
    })

    Column {
        Text(
            text = "User Section",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        MyCheckbox(
            info = postNotificationInfo.value,
            onSelected = {
                viewModel.toggleNotification()
            })
        MyCheckbox(
            info = showDebugOptions.value,
            onSelected = {
                viewModel.showDebugOptions()
            })
        Text(
            text = "Notify at:"
        )
        MyTimePicker(time = notifyAt.value, timeChosen = { time ->
            viewModel.changeNotifyAt(time)
        })
        Text(
            text = "Habit Section",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        MyTextField(info = showRecordsUntil.value, onValueChange = { value ->
            viewModel.changeRecordsUntil(value)
        })
    }
}