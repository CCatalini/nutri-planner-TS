package com.austral.nutri_planner_ts.ui.screens.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.nutri_planner_ts.BuildConfig
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.CommonFood
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
    data class Success(val recipes: List<CommonFood>) : RecipesUiState()
    data object Error : RecipesUiState()
}

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val apiServiceImpl: ApiServiceImpl,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipesUiState>(RecipesUiState.Loading)
    val uiState: StateFlow<RecipesUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    private val baseUrl by lazy {
        context.getString(R.string.nutritionix_url)
    }

    init {
        loadRecipes()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500) // Debounce search
            loadRecipes(query)
        }
    }

    fun retry() = loadRecipes(_searchQuery.value)

    private fun loadRecipes(query: String = "") {
        _uiState.value = RecipesUiState.Loading

        apiServiceImpl.getRecipe(
            baseUrl = baseUrl,
            query = query.ifEmpty { "apple" },
            onSuccess = { recipes ->
                viewModelScope.launch {
                    _uiState.emit(RecipesUiState.Success(recipes))
                }
            },
            onFail = {
                _uiState.value = RecipesUiState.Error
            },
            loadingFinished = { }
        )
    }
}
