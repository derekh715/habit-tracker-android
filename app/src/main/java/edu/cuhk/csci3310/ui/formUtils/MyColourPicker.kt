package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.color.ColorDialog
import com.maxkeppeler.sheets.color.models.ColorConfig
import com.maxkeppeler.sheets.color.models.ColorSelection
import com.maxkeppeler.sheets.color.models.ColorSelectionMode
import com.maxkeppeler.sheets.color.models.SingleColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalStdlibApi::class)
@Composable
fun MyColourPicker(
    colour: Int,
    setColour: (Int) -> Unit,
) {
    val state = rememberUseCaseState(visible = false)
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .background(color = Color(colour))
                    .width(32.dp)
                    .height(32.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "#${colour.toHexString()}")
        Spacer(modifier = Modifier.width(16.dp))
        TextButton(onClick = { state.show() }) {
            Text("Pick Colour")
        }
    }
    ColorDialog(
        state = state,
        selection =
            ColorSelection(
                selectedColor = SingleColor(colour),
                onSelectColor = { setColour(it) },
            ),
        config =
            ColorConfig(
                displayMode = ColorSelectionMode.CUSTOM,
            ),
    )
}
