package com.austral.nutri_planner_ts.ui.screens.day

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.austral.nutri_planner_ts.api.RecipeResponse

class DayViewModel : ViewModel() {
    var meals by mutableStateOf<List<RecipeResponse>>(listOf())
        private set

    // Mock data for testing
    init {
    }

   // fun updateMealEatenStatus(meal: Meal) {
   //     meals = meals.map {
   //         if (it.id == meal.id) {
   //             it.copy(isEaten = !it.isEaten)
   //         } else {
   //             it
   //         }
   //     }
   //     updateMacroTotals()
   // }

    fun onAddMealClick() {
        // TODO: Implement navigation to add meal screen
    }

    private fun updateMacroTotals() {
        // TODO: Implement macro calculations based on eaten meals
    }
}

