package edu.cuhk.csci3310.ui.nav

sealed class Screen(
    val route: String,
) {
    data object HabitList : Screen("habit_list")

    data object HabitDetail : Screen("habit_detail")

    data object AddHabit : Screen("add_habit")

    data object GroupList : Screen("group_list")

    data object AddGroup : Screen("add_group")

    data object Settings : Screen("settings")
}
