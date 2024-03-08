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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController) {
    val navigationList =
        listOf(
            BottomNavBarItemData(
                screen = Screen.Home,
                name = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
            ),
            BottomNavBarItemData(
                screen = Screen.Dashboard,
                name = "Dashboard",
                selectedIcon = Icons.Filled.Info,
                unselectedIcon = Icons.Outlined.Info,
            ),
            BottomNavBarItemData(
                screen = Screen.Settings,
                name = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings,
            ),
            BottomNavBarItemData(
                screen = Screen.Debug,
                name = "Debug",
                selectedIcon = Icons.Filled.Build,
                unselectedIcon = Icons.Outlined.Build,
            ),
        )
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    NavigationBar {
        navigationList.forEachIndexed {
                index, item ->
            val isSelected = selectedIndex == index
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    selectedIndex = index
                    navController.navigate(item.screen.route)
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
