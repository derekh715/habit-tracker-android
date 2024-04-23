package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyRadioGroup(
    items: List<ToggleableInfo>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.padding(vertical = 16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        items.forEach {
                info ->
            MyRadioButton(
                info = info,
                onSelected = { onSelected(it.text) }
            )
        }
    }
}
