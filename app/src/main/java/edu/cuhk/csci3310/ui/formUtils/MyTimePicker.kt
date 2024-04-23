package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker(
    time: LocalTime,
    timeChosen: (LocalTime) -> Unit,
) {
    val state = rememberUseCaseState(visible = false)
    Row {
        Button(onClick = { state.show() }) {
            Text(text = "Pick Time")
        }
    }
    ClockDialog(
        header = Header.Default(title = "Show notification when..."),
        state = state,
        config =
        ClockConfig(
            defaultTime = time,
            is24HourFormat = true
        ),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            timeChosen(LocalTime.of(hours, minutes))
        },
    )
}