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
import edu.cuhk.csci3310.data.GroupListOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListPicker(
    state: UseCaseState,
    onCreateDialog: (UseCaseState) -> Unit,
    listOptions: List<GroupListOption>,
    // the first list represents the indices of newly checked groups
    // the second list represents the indices of newly unchecked items
    onSelect: (List<Int>, List<Int>) -> Unit,
) {
    val lastListOptions =
        listOptions.map {
            ListOption(
                titleText = it.group.name,
                selected = it.selected,
            )
        }
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
                options = lastListOptions,
            ) { indices, _ ->
                val indicesSet = indices.toSet()
                // options only stores the newly added items
                val newlyAdded = mutableListOf<Int>()
                val newlyRemoved = mutableListOf<Int>()
                // if it is initially selected but changed, that means now it is unselected
                // if it is initially unselected but changed, that means now it is selected
                lastListOptions.forEachIndexed { index, it ->
                    val isSelectedThisTime = indicesSet.contains(index)
                    if (isSelectedThisTime && !it.selected) {
                        newlyAdded.add(index)
                    } else if (!isSelectedThisTime && it.selected) {
                        newlyRemoved.add(index)
                    }
                }
                onSelect(newlyAdded, newlyRemoved)
            },
    )
}
