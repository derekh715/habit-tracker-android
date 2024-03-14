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
import edu.cuhk.csci3310.ui.habitList.HabitListEvent
import edu.cuhk.csci3310.ui.habitList.HabitListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
    viewModel: HabitListViewModel = hiltViewModel(),
    addHabitViewModel: AddHabitViewModel = hiltViewModel(),
) {
    val name = addHabitViewModel.title.collectAsState()
    val description = addHabitViewModel.description.collectAsState()
    val times = addHabitViewModel.times.collectAsState()
    val options = addHabitViewModel.options.collectAsState()
    val polarities = addHabitViewModel.polarities.collectAsState()
    val datePickerState = addHabitViewModel.datePickerState

    Column {
        MyTextField(
            info = name.value,
            onValueChange = { addHabitViewModel.changeName(it) },
        )
        MyTextField(
            info = description.value,
            onValueChange = { addHabitViewModel.changeDescription(it) },
        )
        MyDatePicker(datePickerState = datePickerState)

        MyRadioGroup(items = polarities.value, onSelected = { addHabitViewModel.changePolarity(it) })
        MyDropdown(options = options.value, setOption = { addHabitViewModel.changeOption(it.text) })

        MyTextField(
            info = times.value,
            onValueChange = { addHabitViewModel.changeTime(it) },
        )

        Button(onClick = {
            val habit =
                addHabitViewModel.createHabitFromForm(
                    datePickerState = datePickerState,
                ) ?: return@Button
            viewModel.onEvent(
                HabitListEvent.AddHabit(
                    habit,
                ),
            )
        }) {
            Text("Add Habit")
        }
    }
}
