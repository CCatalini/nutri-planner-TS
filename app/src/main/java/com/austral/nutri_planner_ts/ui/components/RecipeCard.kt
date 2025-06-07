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
import coil.compose.AsyncImage
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.ui.theme.Dimensions

enum class RecipeCardVariant {
    Small,
    Medium
}

@Composable
fun RecipeCard(
    ingredient: IngredientSearchResult,
    variant: RecipeCardVariant,
) {

    Card(
        modifier = Modifier
            .padding(Dimensions.PaddingSmall),
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
    ) {
        when (variant) {
            RecipeCardVariant.Small -> SmallCardContent(ingredient)
            RecipeCardVariant.Medium -> MediumCardContent(ingredient)
        }
    }
}

@Composable
fun SmallCardContent(ingredient: IngredientSearchResult) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacerSmall),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(Dimensions.CornerRadiusMedium)
            )
            .fillMaxWidth()
            .height(Dimensions.CardHeightSmall)
    ) {
        AsyncImage(
            model = "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}",
            contentDescription = ingredient.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight()
                .width(Dimensions.CardImageHeightSmall)
                .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
        )

        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(2f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun MediumCardContent(ingredient: IngredientSearchResult) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .height(Dimensions.CardHeightMedium)
            .width(Dimensions.CardWidthMedium)
            .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(Dimensions.CornerRadiusMedium))
    ) {
        AsyncImage(
            model = "https://spoonacular.com/cdn/ingredients_100x100/${ingredient.image}",
            contentDescription = ingredient.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(Dimensions.CardImageHeightMedium)
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall))
        )
        Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(Dimensions.SpacerTiny))
        Text(
            text = "ID: ${ingredient.id}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}