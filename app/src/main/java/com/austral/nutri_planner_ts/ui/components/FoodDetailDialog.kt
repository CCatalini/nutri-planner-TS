package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.IngredientInformation
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun FoodDetailDialog(
    isVisible: Boolean,
    foodItem: IngredientSearchResult?,
    nutritionInfo: IngredientInformation?,
    onDismiss: () -> Unit
) {
    if (isVisible && foodItem != null) {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(Dimensions.DialogMaxHeightLargePercent),
                shape = RoundedCornerShape(Dimensions.CornerRadiusMedium),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimensions.DialogPaddingLarge)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dialog_title_food_details),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.content_description_close),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
                    
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium)
                    ) {
                        item {
                            // Food Image & Name
                            FoodHeaderSection(foodItem)
                        }
                        
                        item {
                            // Basic Info
                            BasicInfoSection(foodItem, nutritionInfo)
                        }
                        
                        if (nutritionInfo != null) {
                            item {
                                // Macronutrients
                                MacronutrientsSection(nutritionInfo)
                            }
                            
                            item {
                                // Other Nutrients
                                OtherNutrientsSection(nutritionInfo)
                            }
                        }
                        
                        if (foodItem.image.startsWith("http")) {
                            item {
                                // Recipe specific info
                                RecipeInfoSection()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodHeaderSection(foodItem: IngredientSearchResult) {
    val isRecipe = foodItem.image.startsWith("http")
    val imageUrl = if (isRecipe) {
        foodItem.image
    } else {
        stringResource(R.string.spoonacular_ingredients_image_url) + foodItem.image
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.CardPaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = foodItem.name,
                modifier = Modifier
                    .size(Dimensions.FoodImageSizeLarge)
                    .clip(RoundedCornerShape(Dimensions.CornerRadiusMedium)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
            
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            
            if (isRecipe) {
                Text(
                    text = stringResource(R.string.food_category_recipe),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BasicInfoSection(foodItem: IngredientSearchResult, nutritionInfo: IngredientInformation?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.section_basic_information),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
            
            if (nutritionInfo != null) {
                InfoRow(
                    stringResource(R.string.label_amount), 
                    stringResource(R.string.amount_unit_format, nutritionInfo.amount.toString(), nutritionInfo.unit)
                )
                if (nutritionInfo.aisle.isNotBlank()) {
                    InfoRow(stringResource(R.string.label_category), nutritionInfo.aisle)
                }
            }
        }
    }
}

@Composable
private fun MacronutrientsSection(nutritionInfo: IngredientInformation) {
    val macros = nutritionInfo.nutrition?.nutrients?.filter { nutrient ->
        nutrient.name in listOf("Calories", "Protein", "Carbohydrates", "Fat")
    } ?: emptyList()
    
    if (macros.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.PaddingMedium)
            ) {
                Text(
                    text = stringResource(R.string.section_macronutrients),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
                
                macros.forEach { nutrient ->
                    InfoRow(
                        nutrient.name,
                        stringResource(R.string.nutrition_format, nutrient.amount, nutrient.unit)
                    )
                }
            }
        }
    }
}

@Composable
private fun OtherNutrientsSection(nutritionInfo: IngredientInformation) {
    val otherNutrients = nutritionInfo.nutrition?.nutrients?.filter { nutrient ->
        nutrient.name !in listOf("Calories", "Protein", "Carbohydrates", "Fat") && nutrient.amount > 0
    }?.take(8) ?: emptyList() // Show up to 8 other nutrients
    
    if (otherNutrients.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.PaddingMedium)
            ) {
                Text(
                    text = stringResource(R.string.section_other_nutrients),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
                
                otherNutrients.forEach { nutrient ->
                    InfoRow(
                        nutrient.name,
                        stringResource(R.string.nutrition_format, nutrient.amount, nutrient.unit)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeInfoSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PaddingMedium)
        ) {
            Text(
                text = stringResource(R.string.section_recipe_info),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
            
            Text(
                text = stringResource(R.string.recipe_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimensions.Tiny),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
} 