package com.austral.nutri_planner_ts.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun NutriplannerTSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = principal_green,
    onPrimary = text2,

    secondary = secondary_dark,
    onSecondary = text3,

    tertiary = tertiary_dark,

    surface = surface_dark,

    background = background_dark,
    onBackground = text3,

    outline = ocean_blue,

    )

private val LightColorScheme = lightColorScheme(
    primary = principal_green,
    onPrimary = secondary_light,

    secondary = secondary_light,
    onSecondary = text2,

    tertiary = secondary_dark,

    surface = surface_light,

    background = background_light,
    onBackground = text1,

    outline = ocean_blue,
    )

