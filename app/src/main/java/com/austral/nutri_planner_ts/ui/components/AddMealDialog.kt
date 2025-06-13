package com.austral.nutri_planner_ts.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun AddMealDialog(
    isVisible: Boolean,
    isSearching: Boolean,
    searchResults: List<IngredientSearchResult>,
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit,
    onAddMeal: (IngredientSearchResult) -> Unit
) {
    if (isVisible) {
        Dialog(onDismissRequest = onDismiss) {
            var searchQuery by remember { mutableStateOf("") }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(Dimensions.DialogMaxHeightPercent),
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
                            text = stringResource(R.string.dialog_title_add_meal),
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
                    
                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text(stringResource(R.string.search_hint_food)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.content_description_search)
                            )
                        },
                        trailingIcon = {
                            if (isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(Dimensions.IconSizeClose),
                                    strokeWidth = Dimensions.ProgressIndicatorStrokeWidth
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
                    
                    // Search Button
                    Button(
                        onClick = { 
                            if (searchQuery.isNotBlank()) {
                                onSearch(searchQuery)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = searchQuery.isNotBlank() && !isSearching
                    ) {
                        Text(stringResource(R.string.button_search))
                    }
                    
                    Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
                    
                    // Search Results
                    if (searchResults.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.search_results_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
                        
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(searchResults) { ingredient ->
                                SearchResultItem(
                                    ingredient = ingredient,
                                    onAddClick = { onAddMeal(ingredient) }
                                )
                            }
                        }
                    } else if (searchQuery.isNotBlank() && !isSearching) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.search_no_results),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(
    ingredient: IngredientSearchResult,
    onAddClick: () -> Unit
) {
    val isRecipe = ingredient.image.startsWith("http")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = Dimensions.SearchResultCardHeight),
        shape = RoundedCornerShape(Dimensions.CornerRadiusSearchCard),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.CardElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.PaddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image - Bigger size with smaller corner radius
            AsyncImage(
                model = buildSearchImageUrl(ingredient),
                contentDescription = ingredient.name,
                modifier = Modifier
                    .size(Dimensions.FoodImageSizeSearchResult)
                    .clip(RoundedCornerShape(Dimensions.CornerRadiusSmall)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(Dimensions.SpacerMedium))
            
            // Content - More space for text
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerTiny)
            ) {
                Text(
                    text = ingredient.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (isRecipe) {
                    Text(
                        text = stringResource(R.string.food_category_recipe),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(Dimensions.SpacerMedium))
            
            // Circular Add Button with + icon
            FloatingActionButton(
                onClick = onAddClick,
                modifier = Modifier.size(Dimensions.AddButtonSize),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.button_add),
                    modifier = Modifier.size(Dimensions.AddButtonIconSize)
                )
            }
        }
    }
}

// Helper function for search result images
@Composable
private fun buildSearchImageUrl(ingredient: IngredientSearchResult): String {
    val isRecipe = ingredient.image.startsWith("http")
    
    return if (isRecipe) {
        // It's a recipe - use the full URL
        ingredient.image
    } else {
        // It's an ingredient - build the Spoonacular ingredients URL
        stringResource(R.string.spoonacular_ingredients_image_url) + ingredient.image
    }
} 