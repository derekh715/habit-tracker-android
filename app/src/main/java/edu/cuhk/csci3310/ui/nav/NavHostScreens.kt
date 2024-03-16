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
import edu.cuhk.csci3310.ui.debug.DebugScreen
import edu.cuhk.csci3310.ui.groupList.GroupListScreen
import edu.cuhk.csci3310.ui.habitDetail.HabitDetailScreen
import edu.cuhk.csci3310.ui.habitList.HabitListScreen

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
            composable(route = Screen.AddHabit.route) {
                AddHabitScreen()
            }
            composable(
                route = Screen.HabitDetail.route + "?habitId={habitId}",
                arguments =
                    listOf(
                        navArgument(name = "habitId") {
                            type = NavType.LongType
                            defaultValue = -1
                        },
                    ),
            ) {
                HabitDetailScreen()
            }
        }

        navigation(
            route = Graph.Groups.name,
            startDestination = Screen.GroupList.route,
        ) {
            composable(route = Screen.GroupList.route) {
                GroupListScreen(navController = navController)
            }
            composable(route = Screen.AddGroup.route) {
                AddGroupScreen()
            }
        }

        composable(
            route = Screen.Debug.route,
        ) {
            DebugScreen(navController = navController)
        }
    }
}
