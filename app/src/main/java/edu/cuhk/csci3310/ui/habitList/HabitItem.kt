package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.Habit

@Composable
fun HabitItem(
    habit: Habit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = habit.title, fontSize = 32.sp)
        Text(text = habit.description ?: "", fontSize = 24.sp)
    }
}
