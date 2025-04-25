package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.austral.nutri_planner_ts.api.CommonFood
import com.austral.nutri_planner_ts.ui.theme.Dimensions

enum class RecipeCardVariant {
    List,
    Dairy,
    Small
}

@Composable
fun RecipeCard(
    food: CommonFood,
    variant: RecipeCardVariant,
    modifier: Modifier = Modifier
) {
    val cardModifier = when (variant) {
        RecipeCardVariant.Small -> modifier
            .width(Dimensions.CardWidthSmall)
            .height(Dimensions.CardHeightSmall)
            .background(MaterialTheme.colorScheme.secondary,RoundedCornerShape(Dimensions.CornerRadiusMedium))
            .padding(Dimensions.PaddingSmall)

        RecipeCardVariant.Dairy -> modifier
            .fillMaxWidth()
            .height(Dimensions.CardHeightDairy)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
            .padding(Dimensions.PaddingSmall)

        RecipeCardVariant.List-> modifier
            .fillMaxWidth()
            .height(Dimensions.CardHeightList)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
            .padding(Dimensions.PaddingSmall)
    }

    val columnArrangement = when (variant) {
        RecipeCardVariant.List -> Arrangement.spacedBy(Dimensions.SpacerMedium, Alignment.Top)
        else -> Arrangement.spacedBy(Dimensions.SpacerSmall, Alignment.Top)
    }

    val columnAlignment = when (variant) {
        RecipeCardVariant.List -> Alignment.CenterHorizontally
        else -> Alignment.Start
    }

    Card(
        modifier = cardModifier,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.Default),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
    ) {
        Column(
            verticalArrangement = columnArrangement,
            horizontalAlignment = columnAlignment,
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimensions.PaddingMedium)
        ) {
            Text(
                text = food.food_name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold

            )
            Text(
                text = "Serving: ${food.serving_qty} ${food.serving_unit}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tag: ${food.tag_name}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
