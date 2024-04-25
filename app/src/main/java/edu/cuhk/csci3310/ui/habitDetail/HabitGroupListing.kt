package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Column(horizontalAlignment = Alignment.Start) {
        Text("In group(s):", fontSize = 24.sp)
        Spacer(Modifier.height(8.dp))
        LazyColumn(content = {
            items(inGroups) {
                Column(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(text = it.group.name, color = Color(it.group.colour))
                }
            }
        })
        Spacer(Modifier.height(24.dp))
        GroupListPicker(
            state = state,
            onCreateDialog = { onCreateDialog() },
            onSelect = onSelect,
            listOptions = groups,
        )
    }
}