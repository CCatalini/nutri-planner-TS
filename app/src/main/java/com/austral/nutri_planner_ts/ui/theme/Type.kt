package com.austral.nutri_planner_ts.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.austral.nutri_planner_ts.R

val Typography = Typography(

    //  Small Style
    titleSmall = TextStyle(
        fontSize = Dimensions.FontSmall,
        fontWeight = FontWeight(500),
    ),
    bodySmall = TextStyle(
        fontSize = Dimensions.FontSmall,
        fontWeight = FontWeight(400),
    ),

    // Medium style
    titleMedium = TextStyle(
        fontSize = Dimensions.FontMedium,
        fontWeight = FontWeight(500),
    ),
    bodyMedium = TextStyle(
        fontSize = Dimensions.FontMedium,
        fontWeight = FontWeight(400),
    ),

    // Large Style
    titleLarge = TextStyle(
        fontSize = Dimensions.FontLarge,
        fontWeight = FontWeight(500),
    ),
    bodyLarge = TextStyle(
        fontSize = Dimensions.FontLarge,
        fontWeight = FontWeight(400),
    ),

)

