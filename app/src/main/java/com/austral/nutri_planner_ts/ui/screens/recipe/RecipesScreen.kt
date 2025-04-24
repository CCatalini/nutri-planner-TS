package com.austral.nutri_planner_ts.ui.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.ui.components.RecipeCard



@Composable
fun Recipes() {
    val viewModel = hiltViewModel<RecipesViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value // funciona si importás `getValue`

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
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
                (uiState as RecipesUiState.Success).recipes.forEach {
                    RecipeCard(food = it)
                }
            }
        }
    }
}
