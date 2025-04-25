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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import com.austral.nutri_planner_ts.ui.theme.ocean_blue

@Composable
fun MacrosSummary() {
    val caloriesConsumed = 200
    val caloriesGoal = 1700
    val proteinConsumed = 2
    val proteinGoal = 53
    val fatConsumed = 10
    val fatGoal = 21
    val carbsConsumed = 100
    val carbsGoal = 131

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
                CircularProgressIndicator(
                    progress = { caloriesConsumed.toFloat() / caloriesGoal },
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    strokeWidth = Dimensions.HeightMedium,
                    modifier = Modifier.size(Dimensions.ProgressIndicatorSizeLarge)
                )
                Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
                Text(
                    text = "$caloriesConsumed / $caloriesGoal",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
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
                    value = proteinConsumed,
                    goal = proteinGoal,
                    color = MaterialTheme.colorScheme.primary // Protein = rojo
                )
                MacroProgressBar(
                    label = stringResource(R.string.fat_label),
                    value = fatConsumed,
                    goal = fatGoal,
                    color = MaterialTheme.colorScheme.primary // Fat = violeta/grisáceo
                )
                MacroProgressBar(
                    label = stringResource(R.string.carbs_label),
                    value = carbsConsumed,
                    goal = carbsGoal,
                    color = MaterialTheme.colorScheme.primary // Carbs = amarillo/naranja
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "$value / $goal g",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.height(Dimensions.SpacerTiny))
        LinearProgressIndicator(
            progress = { value.toFloat() / goal },
            color = color,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimensions.HeightMedium)
                .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
        )
    }
}