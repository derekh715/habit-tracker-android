package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListPicker(
    state: UseCaseState,
    onCreateDialog: (UseCaseState) -> Unit,
    listOptions: List<ListOption>,
    onSelect: (List<Int>, List<ListOption>) -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Button(onClick = { onCreateDialog(state) }) {
            Icon(Icons.Filled.Add, contentDescription = "add to group")
            Text("Add to Group")
        }
    }

    ListDialog(
        state = state,
        selection =
            ListSelection.Multiple(
                showCheckBoxes = true,
                options = listOptions,
            ) { indices, options ->
                onSelect(indices, options)
            },
    )
}
