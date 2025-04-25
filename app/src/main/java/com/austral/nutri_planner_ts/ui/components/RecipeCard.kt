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
    Small,
    Medium
}

@Composable
fun RecipeCard(
    food: CommonFood,
    variant: RecipeCardVariant,
) {

    Card(
        modifier = Modifier
            .padding(Dimensions.PaddingSmall),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
    ) {
        when (variant) {
            RecipeCardVariant.Small -> SmallCardContent(food)
            RecipeCardVariant.Medium -> MediumCardContent(food)
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
            .height(Dimensions.CardHeightMedium)
            .width(Dimensions.CardWidthMedium)
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