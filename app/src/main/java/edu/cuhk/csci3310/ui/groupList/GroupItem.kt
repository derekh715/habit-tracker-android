package edu.cuhk.csci3310.ui.groupList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.Group

@Composable
fun GroupItem(
    group: Group,
    deleteGroup: (group: Group) -> Unit,
) {
    Column {
        Text(text = group.name, fontSize = 32.sp)
        Text(text = group.description ?: "", fontSize = 24.sp)
        Icon(
            Icons.Filled.Delete,
            contentDescription = "remove item",
            modifier =
                Modifier.size(24.dp).clickable {
                    deleteGroup(group)
                },
        )
    }
}
