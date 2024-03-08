package edu.cuhk.csci3310.ui.nav

sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home")

    data object Dashboard : Screen("dashboard")

    data object Debug : Screen("debug")

    data object Settings : Screen("settings")

    data object HabitInfo : Screen("habit")

    data object GroupInfo : Screen("group")

    data object AddHabit : Screen("add_habit")

    data object AddGroup : Screen("add_group")
}
