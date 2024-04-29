package edu.cuhk.csci3310.ui.addHabit

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.cuhk.csci3310.ui.formUtils.MyDatePicker
import edu.cuhk.csci3310.ui.formUtils.MyDropdown
import edu.cuhk.csci3310.ui.formUtils.MyRadioGroup
import edu.cuhk.csci3310.ui.formUtils.MyTextField
import edu.cuhk.csci3310.ui.utils.CommonUiEvent

@Composable
fun AddHabitScreen(viewModel: AddHabitViewModel = hiltViewModel(), navController: NavController) {
    val name = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()
    val times = viewModel.times.collectAsState()
    val options = viewModel.options.collectAsState()
    val polarities = viewModel.polarities.collectAsState()
    val until = viewModel.until.collectAsState()
    val hasHabit = viewModel.habit.collectAsState().value != null
    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect { event ->
            when (event) {
                is CommonUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }

                is CommonUiEvent.ShowToast -> {
                    Toast.makeText(context, event.content, Toast.LENGTH_LONG).show()
                }
            }
        }
    })

    Column {
        MyTextField(
            info = name.value,
            onValueChange = { viewModel.changeName(it) },
        )
        MyTextField(
            info = description.value,
            onValueChange = { viewModel.changeDescription(it) },
        )
        MyDatePicker(until = until.value, dateChosen = { viewModel.changeUntil(it) })

        MyRadioGroup(items = polarities.value, onSelected = { viewModel.changePolarity(it) })
        MyDropdown(options = options.value, setOption = { viewModel.changeOption(it.text) })

        MyTextField(
            info = times.value,
            onValueChange = { viewModel.changeTime(it) },
        )

        Button(onClick = {
            viewModel.onEvent(
                AddHabitEvent.AddHabit,
            )
        }) {
            Text(
                if (hasHabit) {
                    "Change Habit"
                } else {
                    "Add Habit"
                }
            )
        }
    }
}
