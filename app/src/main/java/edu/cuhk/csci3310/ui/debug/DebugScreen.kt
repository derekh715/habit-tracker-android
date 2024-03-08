package edu.cuhk.csci3310.ui.debug

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.cuhk.csci3310.ui.habitList.HabitListEvent
import edu.cuhk.csci3310.ui.habitList.HabitListViewModel

@Composable
fun DebugScreen(
    navController: NavController,
    viewModel: HabitListViewModel = hiltViewModel(),
) {
    Button(onClick = {
        viewModel.onEvent(HabitListEvent.AddDummyHabit)
    }) {
        Text("Add one test item")
    }
}
