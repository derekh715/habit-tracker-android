package edu.cuhk.csci3310.ui.addHabit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Task
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import edu.cuhk.csci3310.ui.formUtils.ToggleableInfo
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import edu.cuhk.csci3310.ui.utils.UiEvent
import edu.cuhk.csci3310.ui.utils.mutableStateIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddHabitViewModel
@Inject
constructor(
    private val habitDao: HabitDao,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiChannel = Channel<UiEvent>()
    val uiChannel = _uiChannel.receiveAsFlow()

    private val prefilledHabit = flow<Long> {
        emit(savedStateHandle.get(key = "habitId") ?: -1)
    }.flatMapLatest { id -> habitDao.getHabitById(id) }

    private fun titleInputInfo(habit: Habit?): TextInputInfo {
        return TextInputInfo(
            value = habit?.title ?: "",
            label = "Habit Name",
            placeholder = "Drink milk every day",
            icon = Icons.Filled.Task,
        )
    }

    val habit = prefilledHabit.map { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val _title = prefilledHabit.map {
        titleInputInfo(it)
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = titleInputInfo(null)
    )
    val title = _title.asStateFlow()

    private fun descriptionInputInfo(habit: Habit?): TextInputInfo {
        return TextInputInfo(
            value = habit?.description ?: "",
            icon = Icons.Filled.Description,
            label = "Description",
            placeholder =
            "It is a simple habit",
        )
    }

    private val _description = prefilledHabit.map {
        descriptionInputInfo(it)
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = descriptionInputInfo(null)
    )
    val description = _description.asStateFlow()

    private fun polaritiesInfo(habit: Habit?): List<ToggleableInfo> {
        val isPositive = habit?.positive ?: true
        return listOf(
            ToggleableInfo(
                text = "Positive",
                toggled = isPositive,
            ),
            ToggleableInfo(
                text = "Negative",
                toggled = !isPositive,
            ),
        )
    }

    private val _polarities = prefilledHabit.map {
        polaritiesInfo(it)
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = polaritiesInfo(null)
    )
    val polarities = _polarities.asStateFlow()

    private fun optionsInfo(habit: Habit?): List<ToggleableInfo> {
        val unit = habit?.frequency?.unit
        return listOf(
            ToggleableInfo(
                text = "Daily",
                toggled = if (unit != null) {
                    unit == FrequencyUnit.DAILY
                } else {
                    true
                },
            ),
            ToggleableInfo(
                text = "Weekly",
                toggled = unit == FrequencyUnit.WEEKLY,
            ),
            ToggleableInfo(
                text = "Monthly",
                toggled = unit == FrequencyUnit.MONTHLY,
            ),
            ToggleableInfo(
                text = "Yearly",
                toggled = unit == FrequencyUnit.YEARLY,
            ),
        )
    }

    private val _options = prefilledHabit.map {
        optionsInfo(it)
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = optionsInfo(null)
    )
    val options = _options.asStateFlow()

    private val _until = prefilledHabit.map {
        it?.until ?: LocalDate.now()
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = LocalDate.now()
    )
    val until = _until.asStateFlow()

    private fun timesInfo(habit: Habit?): TextInputInfo {
        return TextInputInfo(
            value = habit?.frequency?.times?.toString() ?: "1",
            icon = Icons.Filled.Description,
            label = "Times",
            isNumberInput = true,
            helperMessage = "Daily can only be once.",
        )
    }

    private val _times = prefilledHabit.map {
        timesInfo(it)
    }.mutableStateIn(scope = viewModelScope, initialValue = timesInfo(null))
    val times = _times.asStateFlow()

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

    fun changeUntil(newUntil: LocalDate) {
        viewModelScope.launch {
            _until.emit(
                newUntil,
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

    private fun addHabit() {
        viewModelScope.launch {
            // check if titles is filled
            if (title.value.value.isEmpty()) {
                _uiChannel.send(CommonUiEvent.ShowToast("Title is empty"))
                return@launch
            }
            if (until.value <= LocalDate.now()) {
                _uiChannel.send(CommonUiEvent.ShowToast("This habit will end as soon as it starts!"))
                return@launch
            }
            // check if frequency is selected
            val unit =
                when (_options.value.find { it.toggled }!!.text) {
                    "Daily" -> FrequencyUnit.DAILY
                    "Weekly" -> FrequencyUnit.WEEKLY
                    "Monthly" -> FrequencyUnit.MONTHLY
                    "Yearly" -> FrequencyUnit.YEARLY
                    else -> {
                        _uiChannel.send(CommonUiEvent.ShowToast("Frequency is not set!"))
                        return@launch
                    }
                }

            val freq =
                Frequency(
                    unit = unit,
                    times = times.value.value.toInt(),
                )
            val habit =
                Habit(
                    habitId = habit.value?.habitId,
                    description = description.value.value,
                    title = title.value.value,
                    // is positive polarity selected? If yes then it is positive
                    // the polarity can either be positive or negative
                    positive = _polarities.value.first().toggled,
                    until = _until.value,
                    frequency = freq,
                    nextTime = LocalDate.now(),
                )

            habitDao.insertHabit(habit)
            _uiChannel.send(CommonUiEvent.NavigateBack)
        }
    }

    fun onEvent(event: AddHabitEvent) {
        when (event) {
            is AddHabitEvent.AddHabit -> {
                addHabit()
            }
        }
    }

//    private fun sendEvent(event: UiEvent) {
//        viewModelScope.launch {
//            _uiChannel.send(event)
//        }
//    }
}
