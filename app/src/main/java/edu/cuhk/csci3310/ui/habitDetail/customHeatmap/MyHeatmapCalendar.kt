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

fun findLevel(times: Int?): Level {
    if (times == null) {
        return Level.Zero
    }

    // these numbers are arbitrary, if I had time these can be configured by the user
    return when {
        times < -1 -> Level.Negative
        times == -1 -> Level.Skipped
        times == 0 -> Level.Zero
        times == 1 -> Level.One
        times in 2..5 -> Level.Two
        times in 6..10 -> Level.Three
        times >= 11 -> Level.Four
        else -> Level.Zero
    }
}

@Composable
fun MyHeatMapCalendar(heatMap: Map<LocalDate, Int>) {
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
                val level = findLevel(heatMap[day.date])
                DayTile(
                    day = day,
                    week = heatmapWeek,
                    level = level
                )
            },
            weekHeader = { WeekHeader(it) },
            monthHeader = { MonthHeader(it) },
        )
    }
}


