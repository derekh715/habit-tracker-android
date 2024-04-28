package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cuhk.csci3310.data.Habit

@Composable
fun HabitList(
    habits: List<Habit>,
    habitDetail: (Habit) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
        modifier
            .fillMaxWidth(),
    ) {
        items(habits) { habit ->
            HabitItem(
                habit = habit,
                modifier =
                Modifier
                    .clickable { habitDetail(habit) }
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            )
        }
    }
}
