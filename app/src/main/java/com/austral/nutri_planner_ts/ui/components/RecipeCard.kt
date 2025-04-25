package com.austral.nutri_planner_ts.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
) {
    val cardModifier = cardModifierUsingVariant(variant)
    val columnArrangement = getColumnArrangement(variant)
    val columnAlignment = getColumnAlignment(variant)
    val imageModifier = getImageModifier(variant)
    val foodNameStyle = getFoodNameStyle(variant)

    // Card Layout
    Card(
        modifier = cardModifier,
       // elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.Default),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
    ) {
        Column(
         //   verticalArrangement = columnArrangement,
        //    horizontalAlignment = columnAlignment,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimensions.PaddingSmall)
        ) {
            food.photo?.thumb?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = food.food_name,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            }
            Text(
                text = food.food_name,
                style = foodNameStyle,
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


@Composable
private fun cardModifierUsingVariant(variant: RecipeCardVariant): Modifier {
    return when (variant) {
        RecipeCardVariant.Small -> Modifier
            .width(Dimensions.CardWidthSmall)
            .height(Dimensions.CardHeightSmall)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
            .padding(Dimensions.PaddingSmall)

        RecipeCardVariant.Dairy -> Modifier
            .width(Dimensions.CardWidthDairy)
            .height(Dimensions.CardHeightDairy)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
            .padding(Dimensions.PaddingSmall)

        RecipeCardVariant.List -> Modifier
            .fillMaxWidth()
            .height(Dimensions.CardHeightList)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
            .padding(Dimensions.PaddingSmall)
    }
}

@Composable
private fun getColumnArrangement(variant: RecipeCardVariant): Arrangement.Vertical {
    return when (variant) {
        RecipeCardVariant.List -> Arrangement.spacedBy(Dimensions.SpacerMedium, Alignment.Top)
        else -> Arrangement.spacedBy(Dimensions.SpacerSmall, Alignment.Top)
    }
}

@Composable
private fun getColumnAlignment(variant: RecipeCardVariant): Alignment.Horizontal {
    return when (variant) {
        RecipeCardVariant.List -> Alignment.CenterHorizontally
        else -> Alignment.Start
    }
}

@Composable
private fun getImageModifier(variant: RecipeCardVariant): Modifier {
    return when (variant) {
        RecipeCardVariant.List -> Modifier
            .width(Dimensions.CardImageWidthList)
            .height(Dimensions.CardImageHeightList)

        RecipeCardVariant.Dairy -> Modifier
            .width(Dimensions.CardImageWidthDairy)
            .height(Dimensions.CardImageHeightDairy)

        RecipeCardVariant.Small -> Modifier
            .width(Dimensions.CardImageWidthSmall)
            .height(Dimensions.CardImageHeightSmall)
    }.clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
}

@Composable
private fun getFoodNameStyle(variant: RecipeCardVariant): TextStyle {
    return when (variant) {
        RecipeCardVariant.List -> MaterialTheme.typography.titleLarge
        RecipeCardVariant.Dairy -> MaterialTheme.typography.titleMedium
        RecipeCardVariant.Small -> MaterialTheme.typography.titleSmall
    }
}
