package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.input.InputDialog
import com.maxkeppeler.sheets.input.models.Input
import com.maxkeppeler.sheets.input.models.InputHeader
import com.maxkeppeler.sheets.input.models.InputSelection
import com.maxkeppeler.sheets.input.models.InputTextField
import com.maxkeppeler.sheets.input.models.ValidationResult
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDayEntries(
    habit: Habit,
    records: List<Record>,
    changeStatus: (i: Int, r: RecordStatus) -> Unit,
    changeTimes: (r: Record, newTimes: Int) -> Unit,
    changeReason: (r: Record, newReason: String) -> Unit
) {
    val currentRecordIndex = remember { mutableStateOf<Int?>(null) }
    val state = rememberUseCaseState(visible = false)
    val currentRecord = currentRecordIndex.value?.let { records[it] }

    val inputOptions = mutableListOf<Input>()

    if (currentRecord != null) {
        if (currentRecord.status == RecordStatus.FULFILLED) {
            inputOptions.add(
                InputTextField(
                    header = InputHeader(
                        title = "How many times have you finished this habit today?",
                    ),
                    validationListener = { value ->
                        if (value.isNullOrEmpty()) {
                            return@InputTextField ValidationResult.Invalid("Sorry this is empty.")
                        }
                        try {
                            value.toInt()
                            return@InputTextField ValidationResult.Valid
                        } catch (err: NumberFormatException) {
                            return@InputTextField ValidationResult.Invalid("Sorry! This is not a number...")
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    required = true,
                    singleLine = true,
                    key = "times",
                    text = currentRecord.times.toString()
                )
            )
        } else if (currentRecord.status == RecordStatus.SKIPPED) {
            inputOptions.add(
                InputTextField(
                    header = InputHeader(
                        title = "What is the reason for skipping this day?",
                    ),
                    key = "reason",
                    text = currentRecord.reason
                )
            )
        }
    }

    Column {
        Spacer(modifier = Modifier.height(36.dp))
        LazyRow {
            itemsIndexed(records) { index, rec ->
                ToggleableDayEntry(record = rec, changeStatus = {
                    changeStatus(index, it)
                }, showDialog = {
                    currentRecordIndex.value = it
                    if (records[it].status != RecordStatus.UNFULFILLED && records[it].status != RecordStatus.NOTFILLED) {
                        state.show()
                    }
                }, index = index, habit = habit)
            }
        }
        // for inputting skipped reason or completed times
        InputDialog(
            state = state,
            selection = InputSelection(
                input = inputOptions,
                onPositiveClick = {
                    if (currentRecord == null) {
                        return@InputSelection
                    }
                    val times = it.getString("times") ?: ""
                    val reason = it.getString("reason") ?: ""

                    if (currentRecord.status == RecordStatus.FULFILLED) {
                        try {
                            val timesInt = times.toInt()
                            changeTimes(currentRecord, timesInt)
                        } catch (_: NumberFormatException) {
                        }
                    } else if (currentRecord.status == RecordStatus.SKIPPED) {
                        changeReason(currentRecord, reason)
                    }
                }
            ),
        )
    }
}