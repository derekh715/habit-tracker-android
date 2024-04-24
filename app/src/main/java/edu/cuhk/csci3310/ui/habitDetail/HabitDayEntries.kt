package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus

@Composable
fun HabitDayEntries(
    habit: Habit,
    records: List<Record>,
    changeStatus: (i: Int, r: RecordStatus) -> Unit
) {
    Column {
        Text(
            "This habit will end in ${habit.until}",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(36.dp))
        LazyRow {
            itemsIndexed(records) { index, rec ->
                ToggleableDayEntry(record = rec, changeStatus = {
                    changeStatus(index, it)
                })
            }
        }
    }
}