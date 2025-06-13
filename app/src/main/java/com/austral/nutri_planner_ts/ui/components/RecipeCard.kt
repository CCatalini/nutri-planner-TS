package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.trace
import android.util.Log
import coil.compose.AsyncImage
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.IngredientInformation
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun RecipeCard(
    ingredient: IngredientSearchResult,
    nutritionInfo: IngredientInformation?,
    showDeleteButton: Boolean = false,
    onDeleteClick: (() -> Unit)? = null
) = trace("RecipeCard") {
    var showDetailDialog by remember { mutableStateOf(false) }
    val isRecipe = ingredient.image.startsWith("http")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimensions.DayCardHeight)
            .clickable { showDetailDialog = true },
        shape = RoundedCornerShape(Dimensions.CornerRadiusMedium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevation)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Large prominent image like first reference image
                AsyncImage(
                    model = buildImageUrl(ingredient),
                    contentDescription = ingredient.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimensions.DayCardImageHeight)
                        .clip(
                            RoundedCornerShape(
                                topStart = Dimensions.CornerRadiusMedium,
                                topEnd = Dimensions.CornerRadiusMedium,
                                bottomStart = Dimensions.DayCardImageCornerRadius,
                                bottomEnd = Dimensions.DayCardImageCornerRadius
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
                
                // Content section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimensions.DayCardPadding),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerSmall)
                ) {
                    // Title - Larger text
                    Text(
                        text = ingredient.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                    
                    // Nutrition info if available - Reorganized layout
                    if (nutritionInfo != null) {
                        val calories = nutritionInfo.nutrition?.nutrients?.find { it.name == "Calories" }?.amount?.toInt() ?: 0
                        
                        // Calories on left, Recipe indicator on right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.calories_short_format, calories),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.8f)
                            )
                            if (isRecipe) {
                                Text(
                                    text = stringResource(R.string.food_category_recipe),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    } else if (isRecipe) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = stringResource(R.string.food_category_recipe),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Delete button overlay
            if (showDeleteButton && onDeleteClick != null) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Dimensions.SpacerSmall)
                        .size(Dimensions.IconSizeLarge)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(Dimensions.CornerRadiusLarge)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.content_description_remove_meal),
                            modifier = Modifier
                                .padding(Dimensions.SpacerSmall)
                                .size(Dimensions.IconSizeSmall)
                        )
                    }
                }
            }
        }
    }
    
    // Show detail dialog when card is clicked
    FoodDetailDialog(
        isVisible = showDetailDialog,
        foodItem = ingredient,
        nutritionInfo = nutritionInfo,
        onDismiss = { showDetailDialog = false }
    )
}

// Helper function to build the correct image URL
@Composable
private fun buildImageUrl(ingredient: IngredientSearchResult): String {
    Log.d("RecipeCard", "Building image URL for ingredient: ${ingredient.name}")
    Log.d("RecipeCard", "Image field: ${ingredient.image}")
    
    val isRecipe = ingredient.image.startsWith("http")
    
    return if (isRecipe) {
        // It's a recipe - use the full URL
        Log.d("RecipeCard", "Using existing URL for recipe: ${ingredient.image}")
        ingredient.image
    } else {
        // It's an ingredient - build the Spoonacular ingredients URL
        val fullUrl = stringResource(R.string.spoonacular_ingredients_image_url) + ingredient.image
        Log.d("RecipeCard", "Building URL from filename: $fullUrl")
        fullUrl
    }
}