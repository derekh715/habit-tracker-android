package edu.cuhk.csci3310.ui.util

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
    options: List<String>,
    selectedOption: String,
    setOption: (String) -> Unit,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        TextField(value = selectedOption, onValueChange = {}, readOnly = true, label = { Text(text = "") }, trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }, colors = ExposedDropdownMenuDefaults.textFieldColors(), modifier = Modifier.menuAnchor())
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach {
                    option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    setOption(option)
                    expanded = false
                })
            }
        }
    }
}
