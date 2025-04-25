package com.austral.nutri_planner_ts.ui.components

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
    Medium,
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

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
    ) {
        when (variant) {
            RecipeCardVariant.Small -> SmallCardContent(food)
            RecipeCardVariant.Medium -> MediumCardContent(food)
            else -> cardModifierUsingVariant(variant)
        }
    }
}




@Composable
fun SmallCardContent(food: CommonFood) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacerSmall),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
            )
            .width(Dimensions.CardWidthSmall)
            .height(Dimensions.CardHeightSmall)
    ) {
        food.photo?.thumb?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = food.food_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(Dimensions.CardImageHeightSmall)
               //     .weight(1f) // 1/3 del espacio
                    .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
            )
        }

        Text(
            text = food.food_name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(2f) // 2/3 del espacio
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}




@Composable
fun MediumCardContent(food: CommonFood) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
    ) {
        food.photo.thumb.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = food.food_name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(Dimensions.CardImageHeightMedium)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
            )
        }
        Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
        Text(
            text = food.food_name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(Dimensions.SpacerTiny))
        Text(
            text = "${food.serving_qty} min • ${food.serving_unit} kcal",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
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

        RecipeCardVariant.Medium -> Modifier
            .width(Dimensions.CardWidthMedium)
            .height(Dimensions.CardHeightMedium)
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

        RecipeCardVariant.Medium -> Modifier
            .width(Dimensions.CardImageWidthDairy)
            .height(Dimensions.CardImageHeightMedium)

        RecipeCardVariant.Small -> Modifier
            .width(Dimensions.CardImageWidthSmall)
            .height(Dimensions.CardImageHeightSmall)
    }.clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
}

@Composable
private fun getFoodNameStyle(variant: RecipeCardVariant): TextStyle {
    return when (variant) {
        RecipeCardVariant.List -> MaterialTheme.typography.titleLarge
        RecipeCardVariant.Medium -> MaterialTheme.typography.titleMedium
        RecipeCardVariant.Small -> MaterialTheme.typography.titleSmall
    }
}
