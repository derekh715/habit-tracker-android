package edu.cuhk.csci3310.ui.groupList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cuhk.csci3310.data.Group

@Composable
fun GroupItem(
    group: Group,
    deleteGroup: (group: Group) -> Unit,
    changeGroup: (group: Group) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth()
    ) {
        Column {
            Text(text = group.name, fontSize = 32.sp, color = Color(group.colour))
            Text(text = group.description ?: "", fontSize = 24.sp)
        }
        Row {
            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Red),
                onClick = {
                    deleteGroup(group)
                }
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Blue),
                onClick = {
                    changeGroup(group)
                }
            ) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}
