package edu.cuhk.csci3310.ui.habitDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.data.GroupListOption
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.data.HabitGroupCrossRef
import edu.cuhk.csci3310.ui.utils.FetchStatus
import edu.cuhk.csci3310.ui.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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

        private val _item = MutableStateFlow<Habit?>(null)
        val item = _item.asStateFlow()

        private val _groups = MutableStateFlow<List<GroupListOption>>(listOf())
        val groups = _groups.asStateFlow()

        fun initialize() {
            viewModelScope.launch {
                val habitId = savedStateHandle.get<Long>(key = "habitId")!!
                if (habitId == -1L) {
                    _loadingStatus.emit(FetchStatus.NOT_EXISTS)
                } else {
                    _item.emit(habitDao.getHabitById(habitId))
                    _groups.emit(
                        habitDao.getAllGroupsWithIsInGroupOrNot(habitId),
                    )
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
            _item.value?.let {
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
                                habitId = _item.value!!.habitId!!,
                            ),
                        )
                        newList[it].selected = true
                    }
                    event.newlyRemoved.forEach {
                        val option = _groups.value[it]
                        groupDao.removeHabitFromGroup(
                            HabitGroupCrossRef(
                                groupId = option.group.groupId!!,
                                habitId = _item.value!!.habitId!!,
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
            }
        }

        private fun sendEvent(event: HabitDetailScreenUiEvent) {
            viewModelScope.launch {
                _uiChannel.send(event)
            }
        }
    }
