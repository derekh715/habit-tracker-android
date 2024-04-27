package edu.cuhk.csci3310.ui.habitList

import edu.cuhk.csci3310.data.Habit

sealed class HabitListEvent {
    // brings the user to the add habit page
    data object AddHabit : HabitListEvent()

    // brings the user to the habit detail page
    data class HabitDetail(val habit: Habit) : HabitListEvent()

}
