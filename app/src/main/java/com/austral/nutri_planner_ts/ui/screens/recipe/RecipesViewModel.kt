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


sealed class RecipesUiState {
    data object Loading : RecipesUiState()
    data class Success(val recipes: List<CommonFood>) : RecipesUiState()
    data object Error : RecipesUiState()
}

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val apiServiceImpl: ApiServiceImpl,
    @ApplicationContext private val context: Context, // <-- INYECTAR CONTEXTO
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipesUiState>(RecipesUiState.Loading)
    val uiState: StateFlow<RecipesUiState> = _uiState

    private val baseUrl by lazy {
        context.getString(R.string.nutritionix_url) // <-- OBTENER EL STRING REAL
    }

    init {
        loadRecipes()
    }

    fun retry() = loadRecipes()

    private fun loadRecipes() {
        _uiState.value = RecipesUiState.Loading

        apiServiceImpl.getRecipe(
            baseUrl = baseUrl,
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
