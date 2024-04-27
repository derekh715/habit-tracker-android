package edu.cuhk.csci3310.ui.habitList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.ui.nav.Screen
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import edu.cuhk.csci3310.ui.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel
@Inject
constructor(
    private val habitDao: HabitDao,
) : ViewModel() {
    private val _uiChannel = Channel<UiEvent>()
    val uiChannel = _uiChannel.receiveAsFlow()

    fun onEvent(event: HabitListEvent) {
        when (event) {
            is HabitListEvent.AddHabit -> {
                sendEvent(CommonUiEvent.Navigate(Screen.AddHabit.route))
            }

            is HabitListEvent.HabitDetail -> {
                sendEvent(CommonUiEvent.Navigate(Screen.HabitDetail.route + "?habitId=${event.habit.habitId}"))
            }
        }
    }

    val groupsList = habitDao.getAllHabitsWithGroups()
    val habitsList = habitDao.getHabitsOnly()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiChannel.send(event)
        }
    }
}
