package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.Habit

@Composable
fun GroupList(
    groupList: Map<String, List<Habit>>,
    deleteHabit: (Habit) -> Unit,
) {
    val list = groupList.entries.toList()
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(list) { group ->
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(text = group.key, fontSize = 24.sp, modifier = Modifier.padding(bottom = 8.dp))
                Column {
                    group.value.map { habit ->
                        HabitItem(habit = habit, deleteHabit = deleteHabit)
                    }
                }
            }
        }
    }
}
