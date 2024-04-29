package edu.cuhk.csci3310.ui.habitList

import Slate200
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.Habit
import java.time.LocalDate

@Composable
fun HabitItem(
    habit: Habit,
    modifier: Modifier = Modifier,
    borderColour: Color = Slate200
) {
    OutlinedCard(
        modifier = modifier,
        border = BorderStroke(2.dp, borderColour)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(text = habit.title, fontSize = 32.sp)
                Text(
                    text = if (habit.description.isNullOrEmpty()) {
                        "No Description"
                    } else {
                        habit.description
                    },
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            if (habit.nextTime <= LocalDate.now()) {
                Icon(
                    Icons.Filled.Timer,
                    contentDescription = "This habit is due",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
