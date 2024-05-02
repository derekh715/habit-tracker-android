package edu.cuhk.csci3310.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen() {

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        UserSection()
        DebugSection()
    }
}
