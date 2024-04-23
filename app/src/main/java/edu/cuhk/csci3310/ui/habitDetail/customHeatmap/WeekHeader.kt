package edu.cuhk.csci3310.ui.habitDetail.customHeatmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekHeader(dayOfWeek: DayOfWeek) {
    Box(
        modifier = Modifier
            .height(HeatmapCalendarValues.daySize) // Must set a height on the day of week so it aligns with the day.
            .padding(horizontal = 8.dp),
    ) {
        Text(
            text = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
            modifier = Modifier.align(Alignment.Center),
            fontSize = HeatmapCalendarValues.headerFontSize,
        )
    }
}
