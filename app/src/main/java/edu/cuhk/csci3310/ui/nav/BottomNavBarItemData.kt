package edu.cuhk.csci3310.ui.nav

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavBarItemData(
    val name: String,
    val route: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val badgeValue: Int? = null,
    val hasNews: Boolean = false,
)
