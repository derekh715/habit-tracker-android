package edu.cuhk.csci3310.ui.addHabit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.ui.formUtils.MyDatePicker
import edu.cuhk.csci3310.ui.formUtils.MyDropdown
import edu.cuhk.csci3310.ui.formUtils.MyRadioGroup
import edu.cuhk.csci3310.ui.formUtils.MyTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(viewModel: AddHabitViewModel = hiltViewModel()) {
    val name = viewModel.title.collectAsState()
    val description = viewModel.description.collectAsState()
    val times = viewModel.times.collectAsState()
    val options = viewModel.options.collectAsState()
    val polarities = viewModel.polarities.collectAsState()
    val datePickerState = viewModel.datePickerState

    Column {
        MyTextField(
            info = name.value,
            onValueChange = { viewModel.changeName(it) },
        )
        MyTextField(
            info = description.value,
            onValueChange = { viewModel.changeDescription(it) },
        )
        MyDatePicker(datePickerState = datePickerState)

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
            Text("Add Habit")
        }
    }
}
