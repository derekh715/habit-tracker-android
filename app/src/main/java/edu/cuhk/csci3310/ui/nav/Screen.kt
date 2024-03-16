package edu.cuhk.csci3310.ui.nav

sealed class Screen(
    val route: String,
) {
    // habits graph
    data object HabitList : Screen("habit_list")

    data object HabitDetail : Screen("habit_detail")

    data object AddHabit : Screen("add_habit")

    // groups graph
    data object GroupList : Screen("group_list")

    data object GroupInfo : Screen("group")

    data object AddGroup : Screen("add_group")

    // settings graph
    data object Settings : Screen("settings")

    // debug graph
    data object Debug : Screen("debug")
}
