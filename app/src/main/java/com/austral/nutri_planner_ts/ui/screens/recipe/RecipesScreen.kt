package com.austral.nutri_planner_ts.ui.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.ui.components.RecipeCard
import com.austral.nutri_planner_ts.ui.components.SearchBar
import com.austral.nutri_planner_ts.ui.components.SearchBarVariant
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun Recipes() {
    val viewModel = hiltViewModel<RecipesViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle().value
    var searchBarVariant by remember { mutableStateOf(SearchBarVariant.DEFAULT) }

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimensions.PaddingMedium)
                .onFocusChanged { focusState ->
                    searchBarVariant = when {
                        searchQuery.isNotEmpty() -> SearchBarVariant.WITH_TEXT
                        focusState.isFocused -> SearchBarVariant.FOCUSED
                        else -> SearchBarVariant.DEFAULT
                    }
                },
            hint = "Search recipes...",
            variant = searchBarVariant,
            onValueChange = viewModel::onSearchQueryChange,
            onClear = { viewModel.onSearchQueryChange("") }
        )

        when (uiState) {
            is RecipesUiState.Loading -> {
                CircularProgressIndicator(
                    color = Color.Gray,
                    modifier = Modifier.size(48.dp)
                )
            }
            is RecipesUiState.Error -> {
                Text("There was an error")
                Button(onClick = viewModel::retry) {
                    Text("Retry")
                }
            }
            is RecipesUiState.Success -> {
                uiState.recipes.forEach {
                    RecipeCard(food = it)
                }
            }
        }
    }
}
