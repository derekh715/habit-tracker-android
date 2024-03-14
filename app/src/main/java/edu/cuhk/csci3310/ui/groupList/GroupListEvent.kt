package edu.cuhk.csci3310.ui.groupList

import edu.cuhk.csci3310.data.Group
import edu.cuhk.csci3310.data.Habit

sealed class GroupListEvent {
    // brings the user to the add habit page
    data class AddGroup(val group: Group) : GroupListEvent()

    data object AddDummyGroup : GroupListEvent()

    // brings the user to the group info page
    data class ChangeGroup(val group: Group) : GroupListEvent()

    data class RemoveGroup(val group: Group) : GroupListEvent()

    data class AddHabitToGroup(val group: Group, val habit: Habit) : GroupListEvent()
}
