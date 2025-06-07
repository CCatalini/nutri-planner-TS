package com.austral.nutri_planner_ts.ui.screens.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.nutri_planner_ts.BuildConfig
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.manager.ApiServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


sealed class RecipesUiState {
    data object Loading : RecipesUiState()
    data class Success(val ingredients: List<IngredientSearchResult>) : RecipesUiState()
    data object Error : RecipesUiState()
}

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val apiServiceImpl: ApiServiceImpl,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipesUiState>(RecipesUiState.Loading)
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    private val baseUrl by lazy {
        context.getString(R.string.spoonacular_url)
    }

    private var searchJob: Job? = null

    init {
        searchIngredients("")
    }

    fun retry() {
        searchIngredients("")
    }

    fun searchIngredients(query: String) {
        searchJob?.cancel()
        _uiState.value = RecipesUiState.Loading

        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            
            if (query.isNotEmpty()) {
                apiServiceImpl.searchIngredients(
                    query = query,
                    onSuccess = { ingredients ->
                        _uiState.value = RecipesUiState.Success(ingredients)
                    },
                    onFail = {
                        _uiState.value = RecipesUiState.Error
                    },
                    loadingFinished = { }
                )
            } else {
                // Popular ingredient queries for initial display
                val queries = listOf(
                    "chicken", "beef", "salmon", "rice", "pasta",
                    "tomato", "onion", "garlic", "apple", "banana",
                    "cheese", "milk", "egg", "potato", "carrot"
                )

                val collectedIngredients = mutableListOf<IngredientSearchResult>()
                var completed = 0

                queries.forEach { ingredient ->
                    apiServiceImpl.searchIngredients(
                        query = ingredient,
                        onSuccess = { ingredients ->
                            ingredients.firstOrNull()?.let { collectedIngredients.add(it) }
                            completed++
                            if (completed == queries.size) {
                                _uiState.value = RecipesUiState.Success(collectedIngredients)
                            }
                        },
                        onFail = {
                            completed++
                            if (completed == queries.size) {
                                _uiState.value = RecipesUiState.Success(collectedIngredients)
                            }
                        },
                        loadingFinished = { }
                    )
                }
            }
        }
    }
}
