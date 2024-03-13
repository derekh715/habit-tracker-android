package edu.cuhk.csci3310.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import edu.cuhk.csci3310.ui.debug.DebugScreen
import edu.cuhk.csci3310.ui.habitList.AddHabitScreen
import edu.cuhk.csci3310.ui.habitList.HabitListScreen

@Composable
fun NavHostScreens(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Graph.HabitList.name) {
        navigation(
            route = Graph.HabitList.name,
            startDestination = Screen.Home.route,
        ) {
            composable(route = Screen.Home.route) {
                HabitListScreen(navController = navController)
            }
            composable(route = Screen.AddHabit.route) {
                AddHabitScreen()
            }
        }

        composable(
            route = Screen.Debug.route,
        ) {
            DebugScreen(navController = navController)
        }
    }
}
