package edu.cuhk.csci3310.ui.habitDetail

sealed class HabitDetailEvent {
    data object AddToGroupDialog : HabitDetailEvent()

    data class AddToGroup(val newlyAdded: List<Int>, val newlyRemoved: List<Int>) : HabitDetailEvent()
}
