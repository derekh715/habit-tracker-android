package edu.cuhk.csci3310.ui.habitDetail.customHeatmap

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kizitonwose.calendar.compose.HeatMapCalendar
import com.kizitonwose.calendar.compose.heatmapcalendar.rememberHeatMapCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.YearMonth

@Composable
fun MyHeatMapCalendar() {
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
    HeatMapCalendar(
        state = state,
        dayContent = { day, heatmapWeek ->
            DayTile(
                day = day,
                week = heatmapWeek,
                level = Level.One
            )
        },
        weekHeader = { WeekHeader(it) },
        monthHeader = { MonthHeader(it) },
    )
}


