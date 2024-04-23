package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.cuhk.csci3310.data.Habit

@Composable
fun HabitList(
    habits: List<Habit>,
    deleteHabit: (Habit) -> Unit,
    habitDetail: (Habit) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        items(habits) {
                habit ->
            HabitItem(
                habit = habit,
                deleteHabit = deleteHabit,
                modifier =
                    Modifier.clickable { habitDetail(habit) },
            )
        }
    }
}