package com.austral.nutri_planner_ts.ui.screens.day

import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.IngredientInformation
import com.austral.nutri_planner_ts.ui.components.MacroData

sealed class DayUiState {
    data object Loading : DayUiState()
    
    data class Success(
        val meals: List<IngredientSearchResult>,
        val macroData: MacroData,
        val isSearching: Boolean = false,
        val searchResults: List<IngredientSearchResult> = emptyList(),
        val nutritionList: List<IngredientInformation> = emptyList()
    ) : DayUiState()
    
    data object Error : DayUiState()
} 