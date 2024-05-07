package edu.cuhk.csci3310.ui.nav

import androidx.compose.ui.graphics.vector.ImageVector

// this type represents each item in the bottom navigation bar
data class BottomNavBarItemData(
    // the title below the icon
    val name: String,
    // where does it lead to
    val route: String,
    // icon to show when it is not the current page
    val unselectedIcon: ImageVector,
    // icon to show when it is the current page
    val selectedIcon: ImageVector,
    // a small counter around the icon -- we do not have to use this feature in our app
    val badgeValue: Int? = null,
    // a small circle indicating updates from that page -- we do not have to use this
    val hasNews: Boolean = false,
)
