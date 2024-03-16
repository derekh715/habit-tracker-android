package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.models.ListOption
import edu.cuhk.csci3310.ui.utils.FetchStatus

@Composable
fun HabitDetailScreen(viewModel: HabitDetailViewModel = hiltViewModel()) {
    val status = viewModel.loadingStatus.collectAsState()
    val item = viewModel.item.collectAsState(initial = null)
    val groups = viewModel.groups.collectAsState()
    val state = rememberUseCaseState(visible = false)
    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect {
                event ->
            when (event) {
                is HabitDetailScreenUiEvent.ShowAddToGroupDialog -> {
                    state.show()
                }
            }
        }
    })

    LaunchedEffect(key1 = true, block = {
        viewModel.initialize()
    })

    when (status.value) {
        FetchStatus.FINISHED -> {
            val habit = item.value
            habit?.let {
                Column(modifier = Modifier.fillMaxSize()) {
                    // TODO: add dialog
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(habit.title, fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(habit.description ?: "No Description Provided", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        // TODO: completing habits
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("In group(s):", fontSize = 24.sp)
                        Spacer(Modifier.height(8.dp))
                        // TODO: add group listing
                        Spacer(Modifier.height(24.dp))
                        // TODO: group dialog
                        GroupListPicker(
                            state = state,
                            onCreateDialog = {
                                viewModel.onEvent(HabitDetailEvent.AddToGroupDialog)
                            },
                            onSelect = { indices, options ->
                                viewModel.onEvent(HabitDetailEvent.AddToGroup(indices, options))
                            },
                            listOptions =
                                groups.value.map {
                                    ListOption(
                                        titleText = it.name,
                                    )
                                },
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column {
                        // TODO: commit graph
                    }
                }
            }
        }
        FetchStatus.NOT_EXISTS, FetchStatus.ERROR -> {
            // TODO: better error handling
            Text("This item cannot be fetched from the database")
        }
        FetchStatus.FETCHING -> {
            // TODO: loading indicators
            Text("Fetching...")
        }
    }
}
