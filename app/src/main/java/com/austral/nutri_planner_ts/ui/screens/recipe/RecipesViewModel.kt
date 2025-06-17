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
                apiServiceImpl.searchFood(
                    query = query,
                    onSuccess = { results ->
                        _uiState.value = RecipesUiState.Success(results)
                    },
                    onFail = {
                        _uiState.value = RecipesUiState.Error
                    },
                    loadingFinished = { }
                )
            } else {
                // 15 healthy recipe ideas for initial display
                val recipeQueries = listOf(
                    "grilled chicken salad",
                    "quinoa bowl",
                    "baked salmon",
                    "veggie stir fry",
                    "lentil soup",
                    "oatmeal pancakes",
                    "turkey lettuce wraps",
                    "avocado toast",
                    "greek yogurt parfait",
                    "black bean tacos",
                    "zucchini noodles pesto",
                    "chickpea curry",
                    "spinach smoothie",
                    "cauliflower rice stir fry",
                    "sweet potato hash",
                    "kale quinoa salad",
                    "grilled shrimp skewers",
                    "mediterranean buddha bowl",
                    "broccoli cheddar egg muffins",
                    "roasted chickpea salad",
                    "tofu veggie wrap",
                    "banana protein pancakes",
                    "mushroom spinach omelette",
                    "salmon poke bowl"
                )

                val collectedIngredients = mutableListOf<IngredientSearchResult>()
                var completed = 0

                recipeQueries.forEach { recipeQuery ->
                    apiServiceImpl.searchFood(
                        query = recipeQuery,
                        onSuccess = { results ->
                            results.firstOrNull()?.let { collectedIngredients.add(it) }
                            completed++
                            if (completed == recipeQueries.size) {
                                _uiState.value = RecipesUiState.Success(collectedIngredients)
                            }
                        },
                        onFail = {
                            completed++
                            if (completed == recipeQueries.size) {
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
