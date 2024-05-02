package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import edu.cuhk.csci3310.data.GroupListOption

@Composable
fun HabitGroupListing(
    groups: List<GroupListOption>,
    state: UseCaseState,
    onCreateDialog: () -> Unit,
    onSelect: (a: List<Int>, b: List<Int>) -> Unit
) {
    val inGroups = groups.filter { it.selected }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text("In group(s):", style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.height(8.dp))
        LazyRow(
            content = {
                items(inGroups) {
                    Text(
                        text = it.group.name,
                        color = Color(it.group.colour),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        )
        Spacer(Modifier.height(24.dp))
        GroupListPicker(
            state = state,
            onCreateDialog = { onCreateDialog() },
            onSelect = onSelect,
            listOptions = groups,
        )
    }
}