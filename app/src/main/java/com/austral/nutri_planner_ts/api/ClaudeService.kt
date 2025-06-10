package com.austral.nutri_planner_ts.api

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import com.austral.nutri_planner_ts.ui.screens.profile.UserProfile
import com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation
import com.austral.nutri_planner_ts.ui.screens.profile.Gender
import com.austral.nutri_planner_ts.ui.screens.profile.Goal
import com.austral.nutri_planner_ts.ui.theme.ApiConstants

@Singleton
class ClaudeService @Inject constructor() {
    
    suspend fun generateMacroRecommendation(profile: UserProfile): MacroRecommendation {
        // Simulate API call delay
        delay(1000)
        
        // Basic BMR calculation (Mifflin-St Jeor Equation)
        val bmr = if (profile.gender == Gender.MALE) {
            ApiConstants.BMR_WEIGHT_MULTIPLIER * profile.weight + 
            ApiConstants.BMR_HEIGHT_MULTIPLIER * profile.height + 
            ApiConstants.BMR_AGE_MULTIPLIER * profile.age + 
            ApiConstants.BMR_MALE_CONSTANT
        } else {
            ApiConstants.BMR_WEIGHT_MULTIPLIER * profile.weight + 
            ApiConstants.BMR_HEIGHT_MULTIPLIER * profile.height + 
            ApiConstants.BMR_AGE_MULTIPLIER * profile.age + 
            ApiConstants.BMR_FEMALE_CONSTANT
        }
        
        // Activity factor
        val activityFactor = profile.activityLevel.multiplier
        
        // Total Daily Energy Expenditure
        val tdee = bmr * activityFactor
        
        // Adjust for goal
        val calories = when (profile.goal) {
            Goal.LOSE_WEIGHT -> (tdee - ApiConstants.CALORIE_DEFICIT).toInt()
            Goal.GAIN_WEIGHT -> (tdee + ApiConstants.CALORIE_SURPLUS).toInt()
            Goal.MAINTAIN_WEIGHT -> tdee.toInt()
        }
        
        // Macro distribution
        val protein = (calories * ApiConstants.PROTEIN_PERCENTAGE / ApiConstants.CALORIES_PER_GRAM_PROTEIN).toInt()
        val carbs = (calories * ApiConstants.CARBS_PERCENTAGE / ApiConstants.CALORIES_PER_GRAM_CARBS).toInt()
        val fat = (calories * ApiConstants.FAT_PERCENTAGE / ApiConstants.CALORIES_PER_GRAM_FAT).toInt()
        
        return MacroRecommendation(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            explanation = "Based on your profile, here are your recommended daily intake values. " +
                    "This calculation uses the Mifflin-St Jeor equation for BMR and adjusts for your activity level and goals."
        )
    }
} 