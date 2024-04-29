package edu.cuhk.csci3310.ui.theme

import Amber400
import Amber600
import Green400
import Green600
import Red500
import Sky400
import Sky600
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Sky400,
    secondary = Amber400,
    tertiary = Green400,
    error = Red500,

    )

private val LightColorScheme = lightColorScheme(
    primary = Sky600,
    secondary = Amber600,
    tertiary = Green600,
    error = Red500
)

@Composable
fun HabitTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val useDynamicColours = dynamicColor && Build.VERSION.SDK_INT > Build.VERSION_CODES.S
    val view = LocalView.current
    val context = LocalContext.current

    val colorScheme = when {
        useDynamicColours && darkTheme -> dynamicLightColorScheme(context)
        useDynamicColours && !darkTheme -> dynamicLightColorScheme(context)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    Log.i("PUB", "$darkTheme | $useDynamicColours")

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}