package edu.cuhk.csci3310.ui.habitList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.ui.formUtils.MyDropdown
import edu.cuhk.csci3310.ui.formUtils.ToggleableInfo
import edu.cuhk.csci3310.ui.utils.CommonUiEvent

@Composable
fun HabitListScreen(
    viewModel: HabitListViewModel = hiltViewModel(),
    onNavigate: (CommonUiEvent.Navigate) -> Unit,
) {
    val habitsList = viewModel.habitsList.collectAsState(initial = listOf())
    val groupList = viewModel.groupsList.collectAsState(initial = mapOf())
    var option by remember {
        mutableStateOf("All Habits")
    }
    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect { event ->
            when (event) {
                is CommonUiEvent.Navigate -> {
                    onNavigate(event)
                }
            }
        }
    })
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MyDropdown(
            options =
            listOf(
                ToggleableInfo(toggled = option == "All Habits", text = "All Habits"),
                ToggleableInfo(toggled = option == "By Group", text = "By Group"),
            ),
            setOption = {
                option = it.text
            },
        )
        if (option == "All Habits") {
            HabitList(habits = habitsList.value, habitDetail = {
                viewModel.onEvent(HabitListEvent.HabitDetail(it))
            }, modifier = Modifier.weight(1f))
        } else {
            GroupList(groupList = groupList.value, habitDetail = {
                viewModel.onEvent(HabitListEvent.HabitDetail(it))
            }, modifier = Modifier.weight(1f))
        }
        Button(
            onClick = {
                viewModel.onEvent(HabitListEvent.AddHabit)
            },
            modifier =
            Modifier
                .align(Alignment.End)
                .padding(end = 8.dp, bottom = 8.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add New Habit")
            Text(text = "Add New Habit")
        }
    }
}
