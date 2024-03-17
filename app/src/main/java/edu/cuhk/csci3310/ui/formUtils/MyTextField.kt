package edu.cuhk.csci3310.ui.formUtils

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MyTextField(
    info: TextInputInfo,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
    ) {
        TextField(
            value = info.value,
            onValueChange = onValueChange,
            label = {
                Text(text = info.label)
            },
            placeholder = { Text(text = info.placeholder) },
            leadingIcon = {
                info.icon?.let {
                    Icon(info.icon, contentDescription = info.contentDescription)
                }
            },
            supportingText = {
                if (info.showError) {
                    Text(text = info.errorMessage, color = MaterialTheme.colorScheme.error)
                } else {
                    Text(text = info.helperMessage)
                }
            },
            trailingIcon = {
                if (info.showError) {
                    Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions =
                when (info.isNumberInput) {
                    true ->
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                        )
                    false -> KeyboardOptions.Default
                },
        )
    }
}
