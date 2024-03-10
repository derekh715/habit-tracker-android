package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.cuhk.csci3310.ui.nav.Screen

@Composable
fun HabitListScreen(
    viewModel: HabitListViewModel = hiltViewModel(),
    navController: NavController,
) {
    val habits = viewModel.habitsList.collectAsState(initial = listOf())
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) {
            items(habits.value) {
                    habit ->
                HabitItem(habit = habit, deleteHabit = { viewModel.onEvent(HabitListEvent.RemoveHabit(it)) })
            }
        }
        Button(onClick = {
            navController.navigate(Screen.AddHabit.route)
        }, modifier = Modifier.align(Alignment.End).padding(end = 8.dp, bottom = 8.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "Add New Habit")
            Text(text = "Add New Habit")
        }
    }
}
