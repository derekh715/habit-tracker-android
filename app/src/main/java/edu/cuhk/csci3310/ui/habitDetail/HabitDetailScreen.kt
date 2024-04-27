package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import edu.cuhk.csci3310.ui.habitDetail.barChart.BarChart
import edu.cuhk.csci3310.ui.habitDetail.customHeatmap.MyHeatMapCalendar
import edu.cuhk.csci3310.ui.utils.CommonUiEvent

@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val habit = viewModel.habit.collectAsState(initial = null)
    val groups = viewModel.groups.collectAsState(initial = listOf())
    val map = viewModel.dateMap.collectAsState(initial = mutableMapOf())

    if (habit.value == null || groups.value == null) {
        return
    }
    val records = viewModel.records.collectAsState()
    val state = rememberUseCaseState(visible = false)

    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect { event ->
            when (event) {
                is HabitDetailScreenUiEvent.ShowAddToGroupDialog -> {
                    state.show()
                }

                is CommonUiEvent.Navigate -> {
                    navController.navigate(event.route)
                }

                is CommonUiEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    })


    Column(modifier = Modifier.fillMaxSize()) {
        HabitDescription(habit = habit.value!!)
        Spacer(modifier = Modifier.height(16.dp))
        HabitDayEntries(habit = habit.value!!, records = records.value,
            changeStatus = { index, it ->
                viewModel.onEvent(
                    HabitDetailEvent.ChangeRecordStatus(
                        index = index,
                        newStatus = it
                    )
                )
            },
            changeTimes = { r, newTimes ->
                viewModel.onEvent(
                    HabitDetailEvent.ChangeRecord(
                        r.copy(
                            times = newTimes
                        )
                    )
                )
            },
            changeReason = { r, newReason ->
                viewModel.onEvent(
                    HabitDetailEvent.ChangeRecord(
                        r.copy(
                            reason = newReason
                        )
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        HabitGroupListing(
            groups = groups.value!!, state = state,
            onCreateDialog = {
                viewModel.onEvent(HabitDetailEvent.AddToGroupDialog)
            },
            onSelect = { newlyAdded, newlyRemoved ->
                viewModel.onEvent(
                    HabitDetailEvent.AddToGroup(
                        newlyAdded,
                        newlyRemoved
                    )
                )
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
        MyHeatMapCalendar(heatMap = map.value)
        Spacer(modifier = Modifier.height(16.dp))
        BarChart(map = map.value)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { viewModel.onEvent(HabitDetailEvent.ChangeHabit(habit.value!!)) }) {
                Text(text = "Change Habit")
            }
            Button(onClick = { viewModel.onEvent(HabitDetailEvent.RemoveHabit(habit.value!!)) }) {
                Text(text = "Delete Habit")
            }
        }
    }
}
