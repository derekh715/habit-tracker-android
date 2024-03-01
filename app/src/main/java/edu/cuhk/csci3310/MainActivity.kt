package edu.cuhk.csci3310

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import edu.cuhk.csci3310.ui.habitList.HabitListScreen
import edu.cuhk.csci3310.ui.theme.HabitTrackerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackerTheme {
                HabitListScreen()
            }
        }
    }
}
