package edu.cuhk.csci3310.ui.habitDetail

import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.RecordStatus

sealed class HabitDetailEvent {
    data object AddToGroupDialog : HabitDetailEvent()

    data class AddToGroup(val newlyAdded: List<Int>, val newlyRemoved: List<Int>) :
        HabitDetailEvent()

    data class ChangeRecord(val index: Int, val newStatus: RecordStatus) : HabitDetailEvent()

    data class ChangeHabit(val habit: Habit) : HabitDetailEvent()
}
