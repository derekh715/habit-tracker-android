package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.GroupColour
import edu.cuhk.csci3310.data.Habit

@Composable
fun GroupList(
    habitDetail: (Habit) -> Unit,
    groupList: Map<GroupColour, List<Habit>>,
    modifier: Modifier = Modifier
) {
    val list = groupList.entries.toList()
    LazyColumn(modifier = modifier.fillMaxHeight()) {
        items(list) { group ->
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = group.key.groupName,
                    color = Color(group.key.groupColour),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column {
                    group.value.map { habit ->
                        HabitItem(
                            modifier = Modifier
                                .clickable { habitDetail(habit) }
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            habit = habit,
                            borderColour = Color(group.key.groupColour)
                        )
                    }
                }
            }
        }
    }
}
