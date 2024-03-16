package edu.cuhk.csci3310.ui.addHabit

sealed class AddHabitEvent {
    data object AddHabit : AddHabitEvent()
}
