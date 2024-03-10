package edu.cuhk.csci3310.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val navigationList =
        listOf(
            BottomNavBarItemData(
                route = Graph.HabitList.name,
                name = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavBarItemData(
                route = Screen.Dashboard.route,
                name = "Dashboard",
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Outlined.Info,
            ),
            BottomNavBarItemData(
                route = Screen.Settings.route,
                name = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
            ),
            BottomNavBarItemData(
                route = Screen.Debug.route,
                name = "Debug",
                selectedIcon = Icons.Filled.Build,
                unselectedIcon = Icons.Outlined.Build,
            ),
        )
    NavigationBar {
        val navBackstackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackstackEntry?.destination
        navigationList.forEach {
                item ->
            // if the whole expression evaluates to null, coalesce it to false
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        when (isSelected) {
                            true -> item.selectedIcon
                            false -> item.unselectedIcon
                        },
                        contentDescription = item.name,
                    )
                },
                label = { Text(text = item.name) },
            )
        }
    }
}
