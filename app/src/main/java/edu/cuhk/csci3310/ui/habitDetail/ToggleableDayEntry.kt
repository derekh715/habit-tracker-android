package edu.cuhk.csci3310.ui.habitDetail

import android.graphics.drawable.Icon
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus
import java.time.format.DateTimeFormatter

val dayMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

data class RecordStatusProps(
    val colour: Color,
    val icon: ImageVector?,
    val nextStatus: RecordStatus,
)

@Composable
fun ToggleableDayEntry(
    record: Record,
    changeStatus: (RecordStatus) -> Unit,
) {
    val props =
        when (record.status) {
            RecordStatus.NOTFILLED -> RecordStatusProps(colour = Color.Gray, icon = null, nextStatus = RecordStatus.FULFILLED)
            RecordStatus.FULFILLED ->
                RecordStatusProps(
                    colour = Color.Green,
                    icon = Icons.Outlined.Check,
                    nextStatus = RecordStatus.UNFULFILLED,
                )
            RecordStatus.UNFULFILLED ->
                RecordStatusProps(
                    colour = Color.Red,
                    icon = Icons.Outlined.Close,
                    nextStatus = RecordStatus.SKIPPED,
                )
            RecordStatus.SKIPPED ->
                RecordStatusProps(
                    colour = Color.Yellow,
                    icon = Icons.Outlined.Celebration,
                    nextStatus = RecordStatus.NOTFILLED,
                )
        }

    return Column(modifier = Modifier.padding(end = 64.dp)) {
        Text(text = record.date.format(dayMonthFormatter), fontWeight = FontWeight.SemiBold, color = props.colour)
        IconButton(
            onClick = {
                changeStatus(
                    props.nextStatus,
                )
            },
            modifier = Modifier.size(48.dp).border(4.dp, props.colour, shape = CircleShape),
        ) {
            props.icon?.let { Icon(it, contentDescription = null, tint = props.colour) }
        }
    }
}
