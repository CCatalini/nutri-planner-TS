package com.austral.nutri_planner_ts.ui.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.components.RecipeCard
import com.austral.nutri_planner_ts.ui.components.SearchBar
import com.austral.nutri_planner_ts.ui.components.SearchBarVariant
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun Recipes() {
    val viewModel = hiltViewModel<RecipesViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    var searchQuery by remember { mutableStateOf("") }
    var searchBarVariant by remember { mutableStateOf(SearchBarVariant.DEFAULT) }

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
        modifier = Modifier.padding(Dimensions.PaddingMedium)
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    searchBarVariant = when {
                        searchQuery.isNotEmpty() -> SearchBarVariant.WITH_TEXT
                        focusState.isFocused -> SearchBarVariant.FOCUSED
                        else -> SearchBarVariant.DEFAULT
                    }
                },
            hint = stringResource(R.string.search_hint_ingredients),
            variant = searchBarVariant,
            onValueChange = { query ->
                searchQuery = query
                viewModel.searchIngredients(query)
            },
            onClear = { 
                searchQuery = ""
                viewModel.searchIngredients("")
            }
        )

        when (uiState) {
            is RecipesUiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(Dimensions.CircularProgressIndicator)
                )
            }
            is RecipesUiState.Error -> {
                Text(stringResource(R.string.error_api_quota_exceeded))
                Button(onClick = viewModel::retry) {
                    Text(stringResource(R.string.button_retry))
                }
            }
            is RecipesUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(Dimensions.GridColumnsRecipes),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(uiState.ingredients) { ingredient ->
                        RecipeCard(
                            ingredient = ingredient, 
                            nutritionInfo = null, // No nutrition info available in this context
                            showDeleteButton = false
                        )
                    }
                }
            }
        }
    }
}
