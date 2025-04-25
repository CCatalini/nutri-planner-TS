package com.austral.nutri_planner_ts.ui.screens.day

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.ui.components.MacrosSummary
import com.austral.nutri_planner_ts.ui.components.RecipeCard
import com.austral.nutri_planner_ts.ui.components.RecipeCardVariant
import com.austral.nutri_planner_ts.ui.theme.Dimensions

@Composable
fun Day() {
    val viewModel = hiltViewModel<DayViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
        modifier = Modifier
            .padding(Dimensions.PaddingMedium)
            .fillMaxSize()
    ) {
        MacrosSummary()

        Spacer(modifier = Modifier.height(Dimensions.SpacerLarge))

        Text(stringResource(R.string.meals),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground)

        when (uiState) {
            is DayUiState.Loading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(Dimensions.CircularProgressIndicator)
                )
            }
            is DayUiState.Error -> {
                Text("An error occurred while loading meals.")
                Button(onClick = viewModel::retry) {
                    Text("Retry")
                }
            }
            is DayUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Dimensions.SpacerMedium),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.meals) { food ->
                        RecipeCard(food = food, variant = RecipeCardVariant.Small)
                    }
                }
            }
        }
    }
}
