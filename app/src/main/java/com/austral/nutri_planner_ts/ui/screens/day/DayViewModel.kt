package com.austral.nutri_planner_ts.ui.screens.day

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.IngredientInformation
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.manager.ApiServiceImpl
import com.austral.nutri_planner_ts.ui.components.MacroData
import com.austral.nutri_planner_ts.api.Nutrient
import com.austral.nutri_planner_ts.api.Nutrition
import com.austral.nutri_planner_ts.api.CaloricBreakdown
import com.austral.nutri_planner_ts.api.WeightPerServing
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    private val apiServiceImpl: ApiServiceImpl,
    private val profileRepository: com.austral.nutri_planner_ts.ui.screens.profile.ProfileRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<DayUiState>(DayUiState.Loading)
    val uiState: StateFlow<DayUiState> = _uiState.asStateFlow()

    private val baseUrl by lazy {
        context.getString(R.string.spoonacular_url)
    }

    // Dynamic lists for meals and nutrition
    private val currentMeals = mutableListOf<IngredientSearchResult>()
    private val currentNutrition = mutableListOf<IngredientInformation>()

    init {
        loadDayMeals()
    }

    fun retry() {
        loadDayMeals()
    }

    private fun loadDayMeals() {
        _uiState.value = DayUiState.Loading
        
        // Start with empty lists - user will add meals manually
        currentMeals.clear()
        currentNutrition.clear()
        
        val macroData = calculateMacros(currentNutrition)
        _uiState.value = DayUiState.Success(
            meals = currentMeals.toList(),
            macroData = macroData,
            searchResults = emptyList(),
            nutritionList = currentNutrition.toList()
        )
    }

    fun searchMeals(query: String) {
        val currentState = _uiState.value
        if (currentState is DayUiState.Success) {
            _uiState.value = currentState.copy(
                isSearching = true,
                searchResults = emptyList(),
                nutritionList = currentNutrition.toList()
            )
            
            viewModelScope.launch {
                // Use unified search for both ingredients and recipes
                apiServiceImpl.searchFood(
                    query = query,
                    onSuccess = { searchResults ->
                        val updatedState = _uiState.value
                        if (updatedState is DayUiState.Success) {
                            _uiState.value = updatedState.copy(
                                isSearching = false,
                                searchResults = searchResults,
                                nutritionList = currentNutrition.toList()
                            )
                        }
                    },
                    onFail = {
                        val updatedState = _uiState.value
                        if (updatedState is DayUiState.Success) {
                            _uiState.value = updatedState.copy(
                                isSearching = false,
                                searchResults = emptyList(),
                                nutritionList = currentNutrition.toList()
                            )
                        }
                    },
                    loadingFinished = {}
                )
            }
        }
    }

    fun addMealToDay(ingredient: IngredientSearchResult) {
        val currentState = _uiState.value
        if (currentState is DayUiState.Success) {
            // Add ingredient/recipe to current list
            currentMeals.add(ingredient)
            
            // Determine if it's a recipe or ingredient by checking image URL
            val isRecipe = ingredient.image.startsWith("http")
            
            if (isRecipe) {
                // It's a recipe - get recipe nutrition information
                viewModelScope.launch {
                    apiServiceImpl.getRecipeInformation(
                        recipeId = ingredient.id,
                        onSuccess = { recipeInfo ->
                            // Convert RecipeInformation to IngredientInformation for compatibility
                            val compatibleNutrition = convertRecipeToIngredientNutrition(recipeInfo, ingredient)
                            currentNutrition.add(compatibleNutrition)
                            val updatedMacroData = calculateMacros(currentNutrition)
                            _uiState.value = currentState.copy(
                                meals = currentMeals.toList(),
                                macroData = updatedMacroData,
                                searchResults = emptyList(),
                                nutritionList = currentNutrition.toList()
                            )
                        },
                        onFail = {
                            // If it fails, add without macros
                            val updatedMacroData = calculateMacros(currentNutrition)
                            _uiState.value = currentState.copy(
                                meals = currentMeals.toList(),
                                macroData = updatedMacroData,
                                searchResults = emptyList(),
                                nutritionList = currentNutrition.toList()
                            )
                        },
                        loadingFinished = {}
                    )
                }
            } else {
                // It's an ingredient - get ingredient nutrition information
                viewModelScope.launch {
                    apiServiceImpl.getIngredientInformation(
                        ingredientId = ingredient.id,
                        amount = 100.0,
                        unit = "grams",
                        onSuccess = { info ->
                            currentNutrition.add(info)
                            val updatedMacroData = calculateMacros(currentNutrition)
                            _uiState.value = currentState.copy(
                                meals = currentMeals.toList(),
                                macroData = updatedMacroData,
                                searchResults = emptyList(),
                                nutritionList = currentNutrition.toList()
                            )
                        },
                        onFail = {
                            val updatedMacroData = calculateMacros(currentNutrition)
                            _uiState.value = currentState.copy(
                                meals = currentMeals.toList(),
                                macroData = updatedMacroData,
                                searchResults = emptyList(),
                                nutritionList = currentNutrition.toList()
                            )
                        },
                        loadingFinished = {}
                    )
                }
            }
        }
    }

    private fun convertRecipeToIngredientNutrition(
        recipeInfo: com.austral.nutri_planner_ts.api.RecipeInformation, 
        originalIngredient: IngredientSearchResult
    ): IngredientInformation {
        // Convert recipe nutrients to ingredient format
        val nutrients = recipeInfo.nutrition?.nutrients?.map { recipeNutrient ->
            Nutrient(
                name = recipeNutrient.name,
                amount = recipeNutrient.amount,
                unit = recipeNutrient.unit,
                percentOfDailyNeeds = 0.0
            )
        } ?: emptyList()

        val nutrition = Nutrition(
            nutrients = nutrients,
            properties = emptyList(),
            flavonoids = emptyList(),
            caloricBreakdown = CaloricBreakdown(0.0, 0.0, 0.0),
            weightPerServing = WeightPerServing(0.0, "g")
        )

        return IngredientInformation(
            id = recipeInfo.id,
            original = recipeInfo.title,
            originalName = recipeInfo.title,
            name = recipeInfo.title,
            amount = 1.0,
            unit = "serving",
            unitShort = "serving",
            unitLong = "serving",
            possibleUnits = listOf("serving"),
            estimatedCost = null,
            consistency = "solid",
            shoppingListUnits = null,
            aisle = context.getString(R.string.food_category_recipe),
            image = recipeInfo.image ?: "",
            meta = emptyList(),
            nutrition = nutrition
        )
    }

    fun removeMealFromDay(mealToRemove: IngredientSearchResult) {
        val currentState = _uiState.value
        if (currentState is DayUiState.Success) {
            // Find and remove the meal and its corresponding nutrition info
            val mealIndex = currentMeals.indexOf(mealToRemove)
            if (mealIndex != -1) {
                currentMeals.removeAt(mealIndex)
                // Also remove corresponding nutrition info if it exists
                if (mealIndex < currentNutrition.size) {
                    currentNutrition.removeAt(mealIndex)
                }
                
                val updatedMacroData = calculateMacros(currentNutrition)
                _uiState.value = currentState.copy(
                    meals = currentMeals.toList(),
                    macroData = updatedMacroData,
                    nutritionList = currentNutrition.toList()
                )
            }
        }
    }

    private fun calculateMacros(nutritionList: List<IngredientInformation>): MacroData {
        var totalCalories = 0.0
        var totalProtein = 0.0
        var totalFat = 0.0
        var totalCarbs = 0.0

        nutritionList.forEach { nutrition ->
            nutrition.nutrition?.nutrients?.forEach { nutrient ->
                when (nutrient.name) {
                    "Calories" -> totalCalories += nutrient.amount
                    "Protein" -> totalProtein += nutrient.amount
                    "Fat" -> totalFat += nutrient.amount
                    "Carbohydrates" -> totalCarbs += nutrient.amount
                }
            }
        }

        // Initialize with default values - will be updated asynchronously
        var macroData = createMacroDataWithDefaults(totalCalories, totalProtein, totalFat, totalCarbs)
        
        // Get recommended macros (personalized or defaults) asynchronously
        viewModelScope.launch {
            try {
                val recommendation = profileRepository.getRecommendedMacros()
                val updatedMacroData = MacroData(
                    caloriesConsumed = totalCalories.toInt(),
                    caloriesGoal = recommendation.calories,
                    proteinConsumed = totalProtein.toInt(),
                    proteinGoal = recommendation.protein,
                    fatConsumed = totalFat.toInt(),
                    fatGoal = recommendation.fat,
                    carbsConsumed = totalCarbs.toInt(),
                    carbsGoal = recommendation.carbs
                )
                
                // Update the UI state with personalized/default goals
                val currentState = _uiState.value
                if (currentState is DayUiState.Success) {
                    _uiState.value = currentState.copy(macroData = updatedMacroData)
                    
                    // Save daily entry to history
                    saveDailyEntry(updatedMacroData)
                }
            } catch (e: Exception) {
                // If something fails, keep the default macro data already set
            }
        }

        return macroData
    }
    
    /**
     * Creates MacroData with default values while profile data is being loaded
     */
    private fun createMacroDataWithDefaults(
        totalCalories: Double,
        totalProtein: Double,
        totalFat: Double,
        totalCarbs: Double
    ): MacroData {
        return MacroData(
            caloriesConsumed = totalCalories.toInt(),
            caloriesGoal = 2000,  // Will be updated with actual recommendation
            proteinConsumed = totalProtein.toInt(),
            proteinGoal = 100,    // Will be updated with actual recommendation
            fatConsumed = totalFat.toInt(),
            fatGoal = 67,         // Will be updated with actual recommendation
            carbsConsumed = totalCarbs.toInt(),
            carbsGoal = 250       // Will be updated with actual recommendation
        )
    }
    
    private fun saveDailyEntry(macroData: MacroData) {
        viewModelScope.launch {
            try {
                val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    .format(java.util.Date())
                
                val dailyEntry = com.austral.nutri_planner_ts.ui.screens.profile.DailyEntry(
                    date = today,
                    consumedCalories = macroData.caloriesConsumed,
                    consumedProtein = macroData.proteinConsumed,
                    consumedCarbs = macroData.carbsConsumed,
                    consumedFat = macroData.fatConsumed,
                    recommendedCalories = macroData.caloriesGoal,
                    recommendedProtein = macroData.proteinGoal,
                    recommendedCarbs = macroData.carbsGoal,
                    recommendedFat = macroData.fatGoal
                )
                
                profileRepository.addDailyEntry(dailyEntry)
            } catch (e: Exception) {
                // Handle error silently
            }
        }
    }
}

