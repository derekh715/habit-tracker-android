package edu.cuhk.csci3310.ui.habitDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.data.HabitGroupCrossRef
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus
import edu.cuhk.csci3310.ui.nav.Screen
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import edu.cuhk.csci3310.ui.utils.UiEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
class HabitDetailViewModel
@Inject
constructor(
    private val habitDao: HabitDao,
    private val groupDao: GroupDao,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiChannel = Channel<UiEvent>()
    val uiChannel = _uiChannel.receiveAsFlow()

    private val habitId = flow {
        emit(savedStateHandle.get<Long>(key = "habitId") ?: -1)
    }

    private val _habit = habitId.flatMapLatest { id ->
        habitDao.getHabitById(id)
    }
    val habit = _habit.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val _groups =
        habitId.flatMapLatest { id -> habitDao.getAllGroupsWithIsInGroupOrNot(id) }
    val groups = _groups.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    private val _records = habitId.flatMapLatest { id ->
        // don't fetch too much
        habitDao.getRecordsOfHabit(id, LocalDate.now().minusMonths(1))
    }.combine(habitId) { raw, habitId ->
        val toBeInsertedRecords = mutableListOf<Record>()
        var currentDay = LocalDate.now()
        var currentRecordIndex = 0
        // if that day does not have a record, fill it with NOTFILLED record
        val lastMonth = currentDay.minusMonths(1)
        while (currentDay >= lastMonth) {
            if (currentRecordIndex < raw.size && currentDay == raw[currentRecordIndex].date) {
                toBeInsertedRecords.add(raw[currentRecordIndex++])
            } else {
                toBeInsertedRecords.add(
                    Record(
                        date = currentDay,
                        status = RecordStatus.NOTFILLED,
                        reason = null,
                        recordId = null,
                        habitId = habitId,
                        times = 0
                    ),
                )
            }
            currentDay = currentDay.minusDays(1)
        }
        return@combine toBeInsertedRecords.toList()
    }
    val records = _records.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf()
    )

    val dateMap: StateFlow<Map<LocalDate, Int>> = _records.map {
        val map = mutableMapOf<LocalDate, Int>()
        it.forEach { r ->
            map[r.date] = when (r.status) {
                RecordStatus.SKIPPED -> -1
                RecordStatus.UNFULFILLED -> -2
                RecordStatus.FULFILLED -> r.times
                RecordStatus.NOTFILLED -> 0
            }
        }
        return@map map
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = mutableMapOf()
    )

    private fun addToGroup() {
        viewModelScope.launch {
            sendEvent(HabitDetailScreenUiEvent.ShowAddToGroupDialog)
        }
    }

    private fun addHabitToGroup(event: HabitDetailEvent.AddToGroup) {
        viewModelScope.launch {
            if (habit.value == null || groups.value == null) {
                return@launch
            }
            event.newlyAdded.forEach {
                val option = groups.value!![it]
                groupDao.addHabitIntoGroup(
                    HabitGroupCrossRef(
                        groupId = option.group.groupId!!,
                        habitId = habit.value!!.habitId!!,
                    ),
                )
            }
            event.newlyRemoved.forEach {
                val option = groups.value!![it]
                groupDao.removeHabitFromGroup(
                    HabitGroupCrossRef(
                        groupId = option.group.groupId!!,
                        habitId = habit.value!!.habitId!!,
                    ),
                )
            }
        }
    }

    fun onEvent(event: HabitDetailEvent) {
        when (event) {
            is HabitDetailEvent.AddToGroupDialog -> {
                addToGroup()
            }

            is HabitDetailEvent.AddToGroup -> {
                addHabitToGroup(event)
            }

            is HabitDetailEvent.ChangeRecordStatus -> {
                recordStatusChange(event)
            }

            is HabitDetailEvent.ChangeRecord -> {
                recordChange(event)
            }

            is HabitDetailEvent.ChangeHabit -> {
                changeHabit(event)
            }

            is HabitDetailEvent.RemoveHabit -> {
                removeHabit(event)
            }
        }
    }

    private fun changeHabit(event: HabitDetailEvent.ChangeHabit) {
        sendEvent(CommonUiEvent.Navigate(Screen.AddHabit.route + "?habitId=${event.habit.habitId}"))
    }

    private fun recordStatusChange(event: HabitDetailEvent.ChangeRecordStatus) {
        viewModelScope.launch {
            if (habit.value == null || groups.value == null) {
                return@launch
            }
            if (event.newStatus === RecordStatus.NOTFILLED) {
                habitDao.deleteRecord(records.value[event.index])
            } else {
                habitDao.addRecord(
                    records.value[event.index].copy(
                        status = event.newStatus,
                        times = if (event.newStatus === RecordStatus.FULFILLED) {
                            1
                        } else {
                            0
                        }
                    )
                )
            }
        }
    }

    private fun recordChange(event: HabitDetailEvent.ChangeRecord) {
        viewModelScope.launch {
            if (habit.value == null || groups.value == null) {
                return@launch
            }
            habitDao.addRecord(event.record)
        }
    }

    private fun removeHabit(event: HabitDetailEvent.RemoveHabit) {
        viewModelScope.launch {
            if (habit.value == null) {
                return@launch
            }
            habitDao.deleteHabit(event.habit)
            _uiChannel.send(CommonUiEvent.NavigateBack)
        }
    }


    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiChannel.send(event)
        }
    }
}
