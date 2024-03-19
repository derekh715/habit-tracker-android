package edu.cuhk.csci3310.ui.habitDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.data.GroupListOption
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.data.HabitGroupCrossRef
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus
import edu.cuhk.csci3310.ui.utils.FetchStatus
import edu.cuhk.csci3310.ui.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

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

        private val _loadingStatus = MutableStateFlow(FetchStatus.FETCHING)
        val loadingStatus = _loadingStatus.asStateFlow()

        private val _habit = MutableStateFlow<Habit?>(null)
        val habit = _habit.asStateFlow()

        private val _groups = MutableStateFlow<List<GroupListOption>>(listOf())
        val groups = _groups.asStateFlow()

        private val _records = MutableStateFlow<List<Record>>(listOf())
        val records = _records.asStateFlow()

        fun initialize() {
            viewModelScope.launch {
                val habitId = savedStateHandle.get<Long>(key = "habitId")!!
                if (habitId == -1L) {
                    _loadingStatus.emit(FetchStatus.NOT_EXISTS)
                } else {
                    _habit.emit(habitDao.getHabitById(habitId))
                    _groups.emit(
                        habitDao.getAllGroupsWithIsInGroupOrNot(habitId),
                    )
                    // don't fetch too much
                    val rawRecords = habitDao.getRecordsOfHabit(habitId, LocalDate.now().minusMonths(1))
                    val toBeInsertedRecords = mutableListOf<Record>()
                    var currentDay = LocalDate.now()
                    var currentRecordIndex = 0
                    // if that day does not have a record, fill it with NOTFILLED record
                    val lastMonth = currentDay.minusMonths(1)
                    while (currentDay >= lastMonth) {
                        if (currentRecordIndex < rawRecords.size && currentDay == rawRecords[currentRecordIndex].date) {
                            toBeInsertedRecords.add(rawRecords[currentRecordIndex++])
                        } else {
                            toBeInsertedRecords.add(
                                Record(
                                    date = currentDay,
                                    status = RecordStatus.NOTFILLED,
                                    reason = null,
                                    recordId = null,
                                    habitId = habitId,
                                ),
                            )
                        }
                        currentDay = currentDay.minusDays(1)
                    }
                    _records.emit(toBeInsertedRecords)
                    _loadingStatus.emit(FetchStatus.FINISHED)
                }
            }
        }

        private fun addToGroup() {
            viewModelScope.launch {
                sendEvent(HabitDetailScreenUiEvent.ShowAddToGroupDialog)
            }
        }

        private fun addHabitToGroup(event: HabitDetailEvent.AddToGroup) {
            _habit.value?.let {
                viewModelScope.launch {
                    val newList = mutableListOf<GroupListOption>()
                    _groups.value.forEach {
                        newList.add(it.copy())
                    }

                    event.newlyAdded.forEach {
                        val option = _groups.value[it]
                        groupDao.addHabitIntoGroup(
                            HabitGroupCrossRef(
                                groupId = option.group.groupId!!,
                                habitId = _habit.value!!.habitId!!,
                            ),
                        )
                        newList[it].selected = true
                    }
                    event.newlyRemoved.forEach {
                        val option = _groups.value[it]
                        groupDao.removeHabitFromGroup(
                            HabitGroupCrossRef(
                                groupId = option.group.groupId!!,
                                habitId = _habit.value!!.habitId!!,
                            ),
                        )
                        newList[it].selected = false
                    }
                    _groups.emit(newList)
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
                is HabitDetailEvent.ChangeRecord -> {
                    recordStatusChange(event)
                }
            }
        }

        private fun recordStatusChange(event: HabitDetailEvent.ChangeRecord) {
            _habit.value?.let {
                viewModelScope.launch {
                    Log.i("App", "${event.index} ${event.newStatus}")
                    val newList = mutableListOf<Record>()
                    _records.value.forEach { newList.add(it) }
                    newList[event.index] = newList[event.index].copy(status = event.newStatus)

                    when (event.newStatus) {
                        RecordStatus.NOTFILLED -> {
                            Log.i("App", _records.value[event.index].toString())
                            habitDao.deleteRecord(_records.value[event.index])
                        }
                        else -> {
                            val newId = habitDao.addRecord(newList[event.index])
                            // if it is just an update, than newId becomes -1
                            if (newId != -1L) {
                                newList[event.index].recordId = newId
                            }
                        }
                    }
                    _records.emit(newList)
                }
            }
        }

        private fun sendEvent(event: HabitDetailScreenUiEvent) {
            viewModelScope.launch {
                _uiChannel.send(event)
            }
        }
    }
