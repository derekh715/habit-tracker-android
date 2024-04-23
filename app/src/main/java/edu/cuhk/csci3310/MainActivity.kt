package edu.cuhk.csci3310

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.cuhk.csci3310.notifications.DailyNotificationBroadcastReceiver
import edu.cuhk.csci3310.ui.nav.BottomNavBar
import edu.cuhk.csci3310.ui.nav.NavHostScreens
import edu.cuhk.csci3310.ui.theme.HabitTrackerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            HabitTrackerTheme {
                Scaffold(
                    bottomBar = {
                        BottomNavBar(navController = navController)
                    },
                ) {
                        paddingValues ->
                    Box(
                        modifier =
                            Modifier
                                .padding(paddingValues)
                                .padding(horizontal = 32.dp, vertical = 16.dp)
                                .fillMaxSize(),
                    ) {
                        NavHostScreens(navController = navController)
                    }
                }
            }
        }
    }
}
