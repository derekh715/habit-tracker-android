package edu.cuhk.csci3310.ui.habitDetail.customHeatmap

import androidx.compose.runtime.Composable
import com.kizitonwose.calendar.compose.heatmapcalendar.HeatMapWeek
import com.kizitonwose.calendar.core.CalendarDay

@Composable
fun DayTile(
    day: CalendarDay,
    week: HeatMapWeek,
    level: Level,
) {
    Tile(color = level.color)
}
