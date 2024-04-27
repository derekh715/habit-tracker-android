package edu.cuhk.csci3310.ui.habitDetail

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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
    index: Int,
    changeStatus: (RecordStatus) -> Unit,
    showDialog: (Int) -> Unit
) {
    // we want to detect long press and short click at the same time
    val interactionSource = remember { MutableInteractionSource() }
    val viewConfiguration = LocalViewConfiguration.current
    val props =
        when (record.status) {
            RecordStatus.NOTFILLED -> RecordStatusProps(
                colour = Color.Gray,
                icon = null,
                nextStatus = RecordStatus.FULFILLED
            )

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

    LaunchedEffect(key1 = interactionSource, key2 = props) {
        var isLongClick = false

        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    showDialog(index)
                }

                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        changeStatus(
                            props.nextStatus,
                        )
                    }

                }

            }
        }
    }



    return Column(modifier = Modifier.padding(end = 64.dp)) {
        Text(
            text = record.date.format(dayMonthFormatter),
            fontWeight = FontWeight.SemiBold,
            color = props.colour
        )
        IconButton(
            onClick = {},
            interactionSource = interactionSource,
            modifier = Modifier
                .size(48.dp)
                .border(4.dp, props.colour, shape = CircleShape)
        ) {
            props.icon?.let { Icon(it, contentDescription = null, tint = props.colour) }
        }
    }
}
