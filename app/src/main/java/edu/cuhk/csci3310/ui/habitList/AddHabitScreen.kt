package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.ui.util.MyDatePicker
import edu.cuhk.csci3310.ui.util.MyDropdown
import edu.cuhk.csci3310.ui.util.MyRadioButton
import edu.cuhk.csci3310.ui.util.MyTextField
import edu.cuhk.csci3310.ui.util.ToggleableInfo
import java.lang.NumberFormatException
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
fun createHabitFromForm(
    description: String,
    title: String,
    polarities: List<ToggleableInfo>,
    frequency: String,
    times: Int,
    datePickerState: DatePickerState,
): Habit? {
    // check if date is selected
    datePickerState.selectedDateMillis ?: return null

    // check if frequency is selected
    val unit =
        when (frequency) {
            "Daily" -> FrequencyUnit.DAILY
            "Weekly" -> FrequencyUnit.WEEKLY
            "Monthly" -> FrequencyUnit.MONTHLY
            "Yearly" -> FrequencyUnit.YEARLY
            else -> return null
        }

    return Habit(
        description = description,
        title = title,
        // is positive polarity selected? If yes then it is positive
        // the polarity can either be positive or negative
        positive = polarities[0].toggled,
        until = Date(datePickerState.selectedDateMillis!!),
        frequency =
            Frequency(
                unit = unit,
                times = times,
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(viewModel: HabitListViewModel = hiltViewModel()) {
    Column {
        var name by remember {
            mutableStateOf("")
        }
        var description by remember {
            mutableStateOf("")
        }
        val polarities =
            remember {
                mutableStateListOf(
                    ToggleableInfo(
                        toggled = true,
                        text = "Positive",
                    ),
                    ToggleableInfo(
                        toggled = false,
                        text = "Negative",
                    ),
                )
            }
        MyTextField(
            value = name,
            onValueChange = {
                name = it
            },
            icon = Icons.Filled.Task,
            label = "Habit Name",
            placeholder = "Drink milk every day",
        )
        MyTextField(
            value = description,
            onValueChange = { description = it },
            icon = Icons.Filled.Description,
            label = "Description",
            placeholder =
                "It is a simple habit",
        )
        val datePickerState = rememberDatePickerState()
        MyDatePicker(datePickerState)

        polarities.forEach {
                info ->
            MyRadioButton(
                info = info,
                onSelected = {
                    polarities.replaceAll {
                        it.copy(
                            toggled = info.text == it.text,
                        )
                    }
                },
            )
        }

        val options =
            remember {
                mutableStateListOf(
                    "Daily",
                    "Weekly",
                    "Monthly",
                    "Yearly",
                )
            }
        var selectedOption by remember {
            mutableStateOf("Daily")
        }

        MyDropdown(options = options, selectedOption = selectedOption, setOption = {
                option ->
            selectedOption = option
        })

        var times by
            remember {
                mutableStateOf<Int?>(null)
            }

        MyTextField(
            value = times?.toString() ?: "",
            onValueChange = {
                times =
                    try {
                        it.toInt()
                    } catch (error: NumberFormatException) {
                        null
                    }
            },
            icon = Icons.Filled.Description,
            label = "Times",
            isNumberInput = true,
        )

        Button(onClick = {
            val habit =
                createHabitFromForm(
                    times = 3,
                    frequency = selectedOption,
                    title = name,
                    description = description,
                    datePickerState = datePickerState,
                    polarities = polarities,
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
