package com.austral.nutri_planner_ts.api

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import com.austral.nutri_planner_ts.ui.screens.profile.UserProfile
import com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation
import com.austral.nutri_planner_ts.ui.screens.profile.Gender
import com.austral.nutri_planner_ts.ui.screens.profile.Goal

@Singleton
class ClaudeService @Inject constructor() {
    
    suspend fun generateMacroRecommendation(profile: UserProfile): MacroRecommendation {
        // Simulate API call delay
        delay(1000)
        
        // Basic BMR calculation (Mifflin-St Jeor Equation)
        val bmr = if (profile.gender == Gender.MALE) {
            10 * profile.weight + 6.25 * profile.height - 5 * profile.age + 5
        } else {
            10 * profile.weight + 6.25 * profile.height - 5 * profile.age - 161
        }
        
        // Activity factor
        val activityFactor = profile.activityLevel.multiplier
        
        // Total Daily Energy Expenditure
        val tdee = bmr * activityFactor
        
        // Adjust for goal
        val calories = when (profile.goal) {
            Goal.LOSE_WEIGHT -> (tdee - 500).toInt() // 500 calorie deficit
            Goal.GAIN_WEIGHT -> (tdee + 500).toInt() // 500 calorie surplus
            Goal.MAINTAIN_WEIGHT -> tdee.toInt() // maintain weight
        }
        
        // Macro distribution (40% carbs, 30% protein, 30% fat)
        val protein = (calories * 0.30 / 4).toInt() // 4 calories per gram
        val carbs = (calories * 0.40 / 4).toInt()   // 4 calories per gram
        val fat = (calories * 0.30 / 9).toInt()     // 9 calories per gram
        
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