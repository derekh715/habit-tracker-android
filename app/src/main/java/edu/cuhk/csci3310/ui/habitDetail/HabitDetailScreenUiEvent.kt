package edu.cuhk.csci3310.ui.habitDetail

import edu.cuhk.csci3310.ui.utils.UiEvent

sealed class HabitDetailScreenUiEvent : UiEvent() {
    data object ShowAddToGroupDialog : HabitDetailScreenUiEvent()
}
