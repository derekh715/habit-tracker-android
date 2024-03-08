package edu.cuhk.csci3310.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.cuhk.csci3310.ui.debug.DebugScreen
import edu.cuhk.csci3310.ui.habitList.HabitListScreen

@Composable
fun NavHostScreens(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(
            route = Screen.Home.route,
        ) {
            HabitListScreen(navController = navController)
        }

        composable(
            route = Screen.Debug.route,
        ) {
            DebugScreen(navController = navController)
        }
    }
}
