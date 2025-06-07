package com.austral.nutri_planner_ts.ui.screens.day

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.manager.ApiServiceImpl
import com.austral.nutri_planner_ts.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DayUiState {
    data object Loading : DayUiState()
    data class Success(val meals: List<IngredientSearchResult>) : DayUiState()
    data object Error : DayUiState()
}

@HiltViewModel
class DayViewModel @Inject constructor(
    private val apiServiceImpl: ApiServiceImpl,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<DayUiState>(DayUiState.Loading)
    val uiState: StateFlow<DayUiState> = _uiState.asStateFlow()

    private val baseUrl by lazy {
        context.getString(R.string.spoonacular_url)
    }

    init {
        loadDayMeals()
    }

    fun retry() {
        loadDayMeals()
    }

    private fun loadDayMeals() {
        _uiState.value = DayUiState.Loading

        viewModelScope.launch {
            // Meal-focused ingredient searches for the day
            val queries = listOf("egg", "chicken", "broccoli")

            val meals = mutableListOf<IngredientSearchResult>()
            var completed = 0

            queries.forEach { meal ->
                apiServiceImpl.searchIngredients(
                    query = meal,
                    onSuccess = { ingredients ->
                        ingredients.firstOrNull()?.let { meals.add(it) }
                        completed++
                        if (completed == queries.size) {
                            _uiState.value = DayUiState.Success(meals)
                        }
                    },
                    onFail = {
                        completed++
                        if (completed == queries.size) {
                            _uiState.value = DayUiState.Success(meals)
                        }
                    },
                    loadingFinished = {}
                )
            }
        }
    }
}
