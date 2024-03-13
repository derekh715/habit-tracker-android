package edu.cuhk.csci3310.ui.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    label: String,
    placeholder: String = "",
    contentDescription: String = "icon",
    isNumberInput: Boolean = false,
    isError: Boolean = false,
    errorText: String = "",
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(text = label)
            },
            placeholder = { Text(text = placeholder) },
            leadingIcon = {
                Icon(icon, contentDescription = contentDescription)
            },
            supportingText = {
                if (isError) {
                    Text(text = errorText, color = MaterialTheme.colorScheme.error)
                }
            },
            trailingIcon = {
                if (isError) {
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions =
                when (isNumberInput) {
                    true ->
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                        )
                    false -> KeyboardOptions.Default
                },
        )
    }
}
