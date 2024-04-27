package edu.cuhk.csci3310.ui.formUtils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDropdown(
    options: List<ToggleableInfo>,
    setOption: (ToggleableInfo) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    // there will always be one selected option so we assert it as non null
    val selectedOption =
        options.find {
            it.toggled
        } ?: options.first()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        TextField(
            value = selectedOption.text,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option.text) }, onClick = {
                    setOption(option)
                    expanded = false
                })
            }
        }
    }
}
