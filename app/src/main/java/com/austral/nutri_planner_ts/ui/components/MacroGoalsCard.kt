package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.theme.Dimensions
import com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation

@Composable
fun MacroGoalsCard(
    recommendation: MacroRecommendation,
    targetWeight: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevationDefault)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerSmall)
        ) {
            Text(
                text = stringResource(R.string.macro_goals_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Divider(color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f))

            // Target weight
            if (targetWeight > 0f) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.target_weight_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        text = stringResource(
                            R.string.amount_unit_format,
                            targetWeight.toInt().toString(),
                            stringResource(R.string.unit_kilogram)
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            GoalRow(
                label = stringResource(R.string.calories_label),
                value = stringResource(
                    R.string.amount_unit_format,
                    recommendation.calories.toString(),
                    stringResource(R.string.unit_kcal)
                )
            )
            GoalRow(
                label = stringResource(R.string.protein_label),
                value = stringResource(
                    R.string.amount_unit_format,
                    recommendation.protein.toString(),
                    stringResource(R.string.unit_gram)
                )
            )
            GoalRow(
                label = stringResource(R.string.fat_label),
                value = stringResource(
                    R.string.amount_unit_format,
                    recommendation.fat.toString(),
                    stringResource(R.string.unit_gram)
                )
            )
            GoalRow(
                label = stringResource(R.string.carbs_label),
                value = stringResource(
                    R.string.amount_unit_format,
                    recommendation.carbs.toString(),
                    stringResource(R.string.unit_gram)
                )
            )
        }
    }
}

@Composable
private fun GoalRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
} 