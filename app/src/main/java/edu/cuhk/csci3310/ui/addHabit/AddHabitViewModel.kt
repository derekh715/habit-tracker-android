package edu.cuhk.csci3310.ui.addHabit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import edu.cuhk.csci3310.ui.formUtils.ToggleableInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel
    @Inject
    constructor(
        private val habitDao: HabitDao,
    ) : ViewModel() {
        private val _title =
            MutableStateFlow(
                TextInputInfo(
                    value = "Test",
                    label = "Habit Name",
                    placeholder = "Drink milk every day",
                    icon = Icons.Filled.Task,
                ),
            )
        val title = _title.asStateFlow()
        private val _description =
            MutableStateFlow(
                TextInputInfo(
                    value = "Test Description",
                    icon = Icons.Filled.Description,
                    label = "Description",
                    placeholder =
                        "It is a simple habit",
                ),
            )
        val description = _description.asStateFlow()

        private val _polarities =
            MutableStateFlow(
                listOf(
                    ToggleableInfo(
                        text = "Positive",
                        toggled = true,
                    ),
                    ToggleableInfo(
                        text = "Negative",
                        toggled = false,
                    ),
                ),
            )
        val polarities = _polarities.asStateFlow()

        private val _options =
            MutableStateFlow(
                listOf(
                    ToggleableInfo(
                        text = "Daily",
                        toggled = false,
                    ),
                    ToggleableInfo(
                        text = "Weekly",
                        toggled = true,
                    ),
                    ToggleableInfo(
                        text = "Monthly",
                        toggled = false,
                    ),
                    ToggleableInfo(
                        text = "Yearly",
                        toggled = false,
                    ),
                ),
            )
        val options = _options.asStateFlow()

        private val _times =
            MutableStateFlow(
                TextInputInfo(
                    value = "1",
                    icon = Icons.Filled.Description,
                    label = "Times",
                    isNumberInput = true,
                ),
            )
        val times = _times.asStateFlow()

        @OptIn(ExperimentalMaterial3Api::class)
        val datePickerState = DatePickerState(Locale.getDefault())

        fun changeName(newName: String) {
            viewModelScope.launch {
                _title.emit(
                    _title.value.copy(
                        value = newName,
                    ),
                )
            }
        }

        fun changeDescription(newDesc: String) {
            viewModelScope.launch {
                _description.emit(
                    _description.value.copy(
                        value = newDesc,
                    ),
                )
            }
        }

        fun changePolarity(newPolarity: String) {
            viewModelScope.launch {
                _polarities.emit(
                    ToggleableInfo.update(
                        _polarities.value,
                        newPolarity,
                    ),
                )
            }
        }

        fun changeOption(newOption: String) {
            viewModelScope.launch {
                _options
                    .emit(
                        ToggleableInfo.update(
                            _options.value,
                            newOption,
                        ),
                    )
            }
        }

        fun changeTime(newTime: String) {
            if (newTime.isNotEmpty()) {
                try {
                    newTime.toInt()
                } catch (error: NumberFormatException) {
                    return
                }
            }
            viewModelScope.launch {
                _times.emit(
                    _times.value.copy(
                        value = newTime,
                    ),
                )
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        fun addHabit() {
            // check if date is selected
            datePickerState.selectedDateMillis ?: return

            // check if frequency is selected
            val unit =
                when (_options.value.find { it.toggled }!!.text) {
                    "Daily" -> FrequencyUnit.DAILY
                    "Weekly" -> FrequencyUnit.WEEKLY
                    "Monthly" -> FrequencyUnit.MONTHLY
                    "Yearly" -> FrequencyUnit.YEARLY
                    else -> return
                }

            val habit =
                Habit(
                    description = description.value.value,
                    title = title.value.value,
                    // is positive polarity selected? If yes then it is positive
                    // the polarity can either be positive or negative
                    positive = _polarities.value.first().toggled,
                    until = Date(datePickerState.selectedDateMillis!!),
                    frequency =
                        Frequency(
                            unit = unit,
                            times = times.value.value.toInt(),
                        ),
                )

            viewModelScope.launch {
                habitDao.insertHabit(habit)
            }
        }

        fun onEvent(event: AddHabitEvent) {
            when (event) {
                is AddHabitEvent.AddHabit -> {
                    addHabit()
                }
            }
        }
    }
