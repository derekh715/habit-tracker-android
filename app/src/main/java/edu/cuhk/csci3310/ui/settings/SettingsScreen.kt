package edu.cuhk.csci3310.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen() {

    Column {
        UserSection()
        DebugSection()
    }
}
