package edu.cuhk.csci3310.ui.habitDetail.customHeatmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MyHeatMapCalendar(heatMap: Map<LocalDate, Level>) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(4) }
    val endMonth = remember { currentMonth.plusMonths(0) } // get until
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val state = rememberHeatMapCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
    )
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HeatMapCalendar(
            state = state,
            dayContent = { day, heatmapWeek ->
                DayTile(
                    day = day,
                    week = heatmapWeek,
                    level = heatMap[day.date] ?: Level.Zero
                )
            },
            weekHeader = { WeekHeader(it) },
            monthHeader = { MonthHeader(it) },
        )
    }
}


