package edu.cuhk.csci3310.ui.habitDetail.customHeatmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarMonth

@Composable
fun MonthHeader(
    calendarMonth: CalendarMonth
) {
    val title = calendarMonth.yearMonth.toString()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp, start = 2.dp),
    ) {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
    }
}