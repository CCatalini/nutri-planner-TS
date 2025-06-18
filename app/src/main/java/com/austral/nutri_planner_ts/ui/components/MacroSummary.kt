package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Constants
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import com.austral.nutri_planner_ts.ui.theme.ocean_blue

@Composable
fun getDefaultMacroData() = MacroData(
    caloriesConsumed = 0,
    caloriesGoal = stringResource(R.string.default_calories_goal).toInt(),
    proteinConsumed = 0,
    proteinGoal = stringResource(R.string.default_protein_goal).toInt(),
    fatConsumed = 0,
    fatGoal = stringResource(R.string.default_fat_goal).toInt(),
    carbsConsumed = 0,
    carbsGoal = stringResource(R.string.default_carbs_goal).toInt()
)

data class MacroData(
    val caloriesConsumed: Int = 0,
    val caloriesGoal: Int = Constants.DEFAULT_CALORIES_GOAL,
    val proteinConsumed: Int = 0,
    val proteinGoal: Int = Constants.DEFAULT_PROTEIN_GOAL,
    val fatConsumed: Int = 0,
    val fatGoal: Int = Constants.DEFAULT_FAT_GOAL,
    val carbsConsumed: Int = 0,
    val carbsGoal: Int = Constants.DEFAULT_CARBS_GOAL
)

@Composable
fun MacrosSummary(macroData: MacroData = MacroData()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
            )
            .padding(Dimensions.PaddingMedium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Calories Circle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.4f)
            ) {
                val caloriesProgress = macroData.caloriesConsumed.toFloat() / macroData.caloriesGoal
                val isCaloriesOverGoal = caloriesProgress > 1f
                
                CircularProgressIndicator(
                    progress = { caloriesProgress.coerceAtMost(1f) },
                    color = if (isCaloriesOverGoal) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    trackColor = if (isCaloriesOverGoal) 
                        MaterialTheme.colorScheme.error.copy(alpha = 0.2f) 
                    else 
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    strokeWidth = Dimensions.HeightMedium,
                    modifier = Modifier.size(Dimensions.ProgressIndicatorSizeLarge)
                )
                Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
                Text(
                    text = stringResource(
                        R.string.macro_detail_kcal_format,
                        macroData.caloriesConsumed,
                        macroData.caloriesGoal
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isCaloriesOverGoal) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
                Text(
                    text = stringResource(R.string.calories_label),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(Dimensions.SpacerLarge))

            // Macros
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
                modifier = Modifier
                    .weight(0.6f)
            ) {
                MacroProgressBar(
                    label = stringResource(R.string.protein_label),
                    value = macroData.proteinConsumed,
                    goal = macroData.proteinGoal,
                    color = MaterialTheme.colorScheme.primary
                )
                MacroProgressBar(
                    label = stringResource(R.string.fat_label),
                    value = macroData.fatConsumed,
                    goal = macroData.fatGoal,
                    color = MaterialTheme.colorScheme.primary
                )
                MacroProgressBar(
                    label = stringResource(R.string.carbs_label),
                    value = macroData.carbsConsumed,
                    goal = macroData.carbsGoal,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun MacroProgressBar(
    label: String,
    value: Int,
    goal: Int,
    color: androidx.compose.ui.graphics.Color
) {
    val progress = value.toFloat() / goal
    val isOverGoal = progress > 1f
    val excess = if (isOverGoal) value - goal else 0
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isOverGoal) "$label ⚠️" else label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(
                    R.string.macro_detail_gram_format,
                    value,
                    goal
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(Dimensions.SpacerTiny))
        LinearProgressIndicator(
            progress = { if (isOverGoal) 1f else progress },
            color = color,
            trackColor = color.copy(alpha = 0.2f),
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.HeightMedium)
                .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
        )
    }
}