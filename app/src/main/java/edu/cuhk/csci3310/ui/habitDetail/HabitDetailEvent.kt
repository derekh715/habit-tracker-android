package edu.cuhk.csci3310.ui.habitDetail

import com.maxkeppeler.sheets.list.models.ListOption

sealed class HabitDetailEvent {
    data object AddToGroupDialog : HabitDetailEvent()

    data class AddToGroup(val indices: List<Int>, val options: List<ListOption>) : HabitDetailEvent()
}
