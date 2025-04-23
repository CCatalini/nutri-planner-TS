package com.austral.nutri_planner_ts.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography(
    // Title Styles
    headlineLarge = TextStyle(
        fontSize = Dimensions.FontExtraLarge,
        fontWeight = FontWeight.Bold
    ),
    headlineMedium = TextStyle(
        fontSize = Dimensions.FontLarge,
        fontWeight = FontWeight.SemiBold
    ),

    // Body Styles
    bodyLarge = TextStyle(
        fontSize = Dimensions.FontLarge,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = Dimensions.FontMedium,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontSize = Dimensions.FontSmall,
        fontWeight = FontWeight.Normal
    ),

    // Label Styles
    labelLarge = TextStyle(
        fontSize = Dimensions.FontLarge,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = TextStyle(
        fontSize = Dimensions.FontMedium,
        fontWeight = FontWeight.Medium
    ),
    labelSmall = TextStyle(
        fontSize = Dimensions.FontSmall,
        fontWeight = FontWeight.Medium
    ),
)