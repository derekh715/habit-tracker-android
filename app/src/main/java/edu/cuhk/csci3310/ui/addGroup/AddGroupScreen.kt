package edu.cuhk.csci3310.ui.addGroup

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import edu.cuhk.csci3310.ui.formUtils.MyColourPicker
import edu.cuhk.csci3310.ui.formUtils.MyTextField

@Composable
fun AddGroupScreen(viewModel: AddGroupViewModel = hiltViewModel()) {
    val name = viewModel.name.collectAsState()
    val description = viewModel.description.collectAsState()
    val colour = viewModel.colour.collectAsState()

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
            Text("Add Group")
        }
    }
}
