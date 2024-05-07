package edu.cuhk.csci3310.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import edu.cuhk.csci3310.ui.addGroup.AddGroupScreen
import edu.cuhk.csci3310.ui.addHabit.AddHabitScreen
import edu.cuhk.csci3310.ui.groupList.GroupListScreen
import edu.cuhk.csci3310.ui.habitDetail.HabitDetailScreen
import edu.cuhk.csci3310.ui.habitList.HabitListScreen
import edu.cuhk.csci3310.ui.settings.SettingsScreen

@Composable
fun NavHostScreens(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Graph.Habits.name) {
        navigation(
            route = Graph.Habits.name,
            startDestination = Screen.HabitList.route,
        ) {
            composable(route = Screen.HabitList.route) {
                HabitListScreen(onNavigate = {
                    navController.navigate(it.route)
                })
            }
            composable(route = Screen.AddHabit.route + "?habitId={habitId}",
                arguments =
                listOf(
                    navArgument(name = "habitId") {
                        type = NavType.LongType
                        // -1 means that we are adding a group instead of
                        // changing an existing group
                        defaultValue = -1
                    }
                )
            ) {
                AddHabitScreen(navController = navController)
            }
            composable(
                route = Screen.HabitDetail.route + "?habitId={habitId}",
                arguments =
                listOf(
                    navArgument(name = "habitId") {
                        type = NavType.LongType
                        // -1 means the habitId is invalid
                        // which should not happen anyway
                        // but it it really happens the view model will handle it gracefully
                        defaultValue = -1
                    },
                ),
            ) {
                HabitDetailScreen(navController = navController)
            }
        }

        navigation(
            route = Graph.Groups.name,
            startDestination = Screen.GroupList.route,
        ) {
            composable(route = Screen.GroupList.route) {
                GroupListScreen(navController = navController)
            }
            composable(route = Screen.AddGroup.route + "?groupId={groupId}",
                arguments =
                listOf(
                    navArgument(name = "groupId") {
                        type = NavType.LongType
                        // -1 means that we are adding a group instead of
                        // changing an existing group
                        defaultValue = -1
                    }
                )
            ) {
                AddGroupScreen(navController = navController)
            }
        }

        // settings only has a single page, so we directly use a composable
        composable(
            route = Screen.Settings.route,
        ) {
            SettingsScreen()
        }
    }
}
