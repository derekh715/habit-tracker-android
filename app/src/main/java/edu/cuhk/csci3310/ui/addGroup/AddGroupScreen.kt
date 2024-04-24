package edu.cuhk.csci3310.ui.addGroup

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.cuhk.csci3310.ui.formUtils.MyColourPicker
import edu.cuhk.csci3310.ui.formUtils.MyTextField
import edu.cuhk.csci3310.ui.utils.CommonUiEvent

@Composable
fun AddGroupScreen(viewModel: AddGroupViewModel = hiltViewModel(), navController: NavController) {
    val name = viewModel.name.collectAsState()
    val description = viewModel.description.collectAsState()
    val colour = viewModel.colour.collectAsState()
    val id = viewModel.prefilledId.collectAsState().value

    LaunchedEffect(key1 = true, block = {
        viewModel.uiChannel.collect { event ->
            when (event) {
                is CommonUiEvent.Navigate -> {
                    navController.popBackStack()
                }
            }
        }
    })

    Column {
        MyTextField(
            info = name.value,
            onValueChange = { viewModel.changeName(it) },
        )
        MyTextField(
            info = description.value,
            onValueChange = { viewModel.changeDescription(it) },
        )
        MyColourPicker(colour = colour.value, setColour = { viewModel.changeColour(it) })
        Button(onClick = {
            viewModel.onEvent(
                AddGroupEvent.AddGroup,
            )
        }) {
            Text(
                if (id != null) {
                    "Change Group"
                } else {
                    "Add Group"
                }
            )
        }
    }
}
