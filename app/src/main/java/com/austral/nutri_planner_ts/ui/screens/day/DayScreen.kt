package com.austral.nutri_planner_ts.ui.screens.day

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.components.MacrosSummary
import com.austral.nutri_planner_ts.ui.components.RecipeCard
import com.austral.nutri_planner_ts.ui.components.AddMealDialog
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun Day() {
    val viewModel = hiltViewModel<DayViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var showAddMealDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
        modifier = Modifier
            .padding(Dimensions.PaddingMedium)
            .fillMaxSize()
    ) {
        // Show MacrosSummary with calculated data when available
        when (uiState) {
            is DayUiState.Success -> {
                MacrosSummary(macroData = uiState.macroData)
            }
            else -> {
                MacrosSummary() // Show default data while loading or on error
            }
        }

        Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))

        // Meals section header with Add button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.meals),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            FloatingActionButton(
                onClick = { showAddMealDialog = true },
                modifier = Modifier.size(Dimensions.FoodImageSize),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.button_add_meal),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        when (uiState) {
            is DayUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(Dimensions.CircularProgressIndicator)
                    )
                }
            }
            is DayUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(stringResource(R.string.error_general))
                    Spacer(modifier = Modifier.height(Dimensions.SpacerMedium))
                    Button(onClick = viewModel::retry) {
                        Text(stringResource(R.string.button_retry))
                    }
                }
            }
            is DayUiState.Success -> {
                if (uiState.meals.isEmpty()) {
                    // Empty state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.empty_meals_title),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(Dimensions.SpacerSmall))
                            Text(
                                text = stringResource(R.string.empty_meals_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        }
                    }
                } else {
                    // Meals list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerLarge),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.meals.zip(uiState.nutritionList)) { (meal, nutrition) ->
                            RecipeCard(
                                ingredient = meal,
                                nutritionInfo = nutrition,
                                showDeleteButton = true,
                                onDeleteClick = { 
                                    viewModel.removeMealFromDay(meal)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Meal Dialog
    if (uiState is DayUiState.Success) {
        AddMealDialog(
            isVisible = showAddMealDialog,
            isSearching = uiState.isSearching,
            searchResults = uiState.searchResults,
            onDismiss = { 
                showAddMealDialog = false
            },
            onSearch = { query ->
                viewModel.searchMeals(query)
            },
            onAddMeal = { ingredient ->
                viewModel.addMealToDay(ingredient)
                showAddMealDialog = false
            }
        )
    }
}
