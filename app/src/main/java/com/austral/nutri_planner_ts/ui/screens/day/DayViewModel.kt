package com.austral.nutri_planner_ts.ui.screens.day

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.nutri_planner_ts.api.CommonFood
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
    data class Success(val meals: List<CommonFood>) : DayUiState()
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
        context.getString(R.string.nutritionix_url)
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
            val queries = listOf("Potato eggs scramble", "Teriyaki Chicken", "Veggie Stir Fry")

            val meals = mutableListOf<CommonFood>()
            var completed = 0

            queries.forEach { meal ->
                apiServiceImpl.getRecipe(
                    baseUrl = baseUrl,
                    query = meal,
                    onSuccess = { foods ->
                        foods.firstOrNull()?.let { meals.add(it) }
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
