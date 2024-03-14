package edu.cuhk.csci3310.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.cuhk.csci3310.ui.addHabit.AddHabitScreen
import edu.cuhk.csci3310.ui.debug.DebugScreen
import edu.cuhk.csci3310.ui.groupList.GroupListScreen
import edu.cuhk.csci3310.ui.habitList.HabitListScreen

@Composable
fun NavHostScreens(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Graph.Habits.name) {
        navigation(
            route = Graph.Habits.name,
            startDestination = Screen.HabitList.route,
        ) {
            composable(route = Screen.HabitList.route) {
                HabitListScreen(navController = navController)
            }
            composable(route = Screen.AddHabit.route) {
                AddHabitScreen()
            }
        }

        navigation(
            route = Graph.Groups.name,
            startDestination = Screen.GroupList.route,
        ) {
            composable(route = Screen.GroupList.route) {
                GroupListScreen(navController = navController)
            }
//            composable(route = Screen.AddGroup.route) {
//                AddGroupScreen()
//            }
        }

        composable(
            route = Screen.Debug.route,
        ) {
            DebugScreen(navController = navController)
        }
    }
}
