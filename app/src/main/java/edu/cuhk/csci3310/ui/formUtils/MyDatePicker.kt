package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    until: LocalDate,
    dateChosen: (LocalDate) -> Unit,
) {
    val state = rememberUseCaseState(visible = false)
    Row {
        Button(onClick = { state.show() }) {
            Text(text = "Pick Date and Time")
        }
    }
    CalendarDialog(
        header =
        Header.Default(title = "This habit will end in"),
        state = state,
        config =
        CalendarConfig(
            yearSelection = true,
            monthSelection = true,
            cameraDate = LocalDate.now().plusMonths(1)
        ),
        selection =
        CalendarSelection.Date(
            selectedDate = until,
        ) {
            dateChosen(it)
        },
    )
}
