package com.austral.nutri_planner_ts.ui.theme

object ApiConstants {
    // Spoonacular API URLs
    const val SPOONACULAR_BASE_URL = "https://api.spoonacular.com/"
    const val SPOONACULAR_RECIPE_DEFAULT_IMAGE = "https://spoonacular.com/recipeImages/default.jpg"
    
    // BMR Calculation Constants
    const val BMR_MALE_CONSTANT = 5
    const val BMR_FEMALE_CONSTANT = -161
    const val BMR_WEIGHT_MULTIPLIER = 10
    const val BMR_HEIGHT_MULTIPLIER = 6.25
    const val BMR_AGE_MULTIPLIER = -5
    
    // Calorie Adjustment Constants
    const val CALORIE_DEFICIT = 500
    const val CALORIE_SURPLUS = 500
    
    // Macro Distribution Constants
    const val CARBS_PERCENTAGE = 0.40
    const val PROTEIN_PERCENTAGE = 0.30
    const val FAT_PERCENTAGE = 0.30
    
    // Calorie per gram constants
    const val CALORIES_PER_GRAM_PROTEIN = 4
    const val CALORIES_PER_GRAM_CARBS = 4
    const val CALORIES_PER_GRAM_FAT = 9
    
    // Activity Level Multipliers
    const val SEDENTARY_MULTIPLIER = 1.2f
    const val LIGHTLY_ACTIVE_MULTIPLIER = 1.375f
    const val MODERATELY_ACTIVE_MULTIPLIER = 1.55f
    const val VERY_ACTIVE_MULTIPLIER = 1.725f
    const val EXTREMELY_ACTIVE_MULTIPLIER = 1.9f
} 