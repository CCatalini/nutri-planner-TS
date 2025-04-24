package com.austral.nutri_planner_ts.api


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.austral.nutri_planner_ts.ui.components.RecipeCard


@Composable
fun Api() {
    val viewModel = hiltViewModel<ApiViewModel>()
    val characters by viewModel.recipes.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val retry by viewModel.showRetry.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
        } else if (retry) {
            Text(
                "There was an error"
            )
            Button(
                onClick = viewModel::retryApiCall
            ) {
                Text(
                    "Retry"
                )
            }
        } else {
            characters.forEach {
                RecipeCard(food = it)
            }
        }
    }
}

