package edu.cuhk.csci3310.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyRadioButton(
    onSelected: (ToggleableInfo) -> Unit,
    info: ToggleableInfo,
) {
    Row(
        modifier =
            Modifier.clickable {
                onSelected(info)
            }.padding(vertical = 8.dp),
    ) {
        RadioButton(selected = info.toggled, onClick = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = info.text)
    }
}
