package com.austral.nutri_planner_ts.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.nutri_planner_ts.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.austral.nutri_planner_ts.api.ClaudeService

data class UserProfile(
    val age: Int = 0,
    val gender: Gender = Gender.FEMALE,
    val weight: Float = 0f,
    val height: Float = 0f,
    val activityLevel: ActivityLevel = ActivityLevel.SEDENTARY,
    val goal: Goal = Goal.MAINTAIN_WEIGHT,
    val targetWeight: Float = 0f,
    val timeFrame: Int = 0, // weeks
    val weeklyWorkouts: Int = 0
)

enum class Gender(val stringResourceId: Int) {
    MALE(com.austral.nutri_planner_ts.R.string.edit_profile_gender_male),
    FEMALE(com.austral.nutri_planner_ts.R.string.edit_profile_gender_female)
}

enum class ActivityLevel(val stringResourceId: Int, val multiplier: Float) {
    SEDENTARY(com.austral.nutri_planner_ts.R.string.activity_level_sedentary, com.austral.nutri_planner_ts.ui.theme.ApiConstants.SEDENTARY_MULTIPLIER),
    LIGHTLY_ACTIVE(com.austral.nutri_planner_ts.R.string.activity_level_lightly_active, com.austral.nutri_planner_ts.ui.theme.ApiConstants.LIGHTLY_ACTIVE_MULTIPLIER),
    MODERATELY_ACTIVE(com.austral.nutri_planner_ts.R.string.activity_level_moderately_active, com.austral.nutri_planner_ts.ui.theme.ApiConstants.MODERATELY_ACTIVE_MULTIPLIER),
    VERY_ACTIVE(com.austral.nutri_planner_ts.R.string.activity_level_very_active, com.austral.nutri_planner_ts.ui.theme.ApiConstants.VERY_ACTIVE_MULTIPLIER),
    EXTREMELY_ACTIVE(com.austral.nutri_planner_ts.R.string.activity_level_extremely_active, com.austral.nutri_planner_ts.ui.theme.ApiConstants.EXTREMELY_ACTIVE_MULTIPLIER)
}

enum class Goal(val stringResourceId: Int) {
    LOSE_WEIGHT(com.austral.nutri_planner_ts.R.string.goal_lose_weight),
    MAINTAIN_WEIGHT(com.austral.nutri_planner_ts.R.string.goal_maintain_weight),
    GAIN_WEIGHT(com.austral.nutri_planner_ts.R.string.goal_gain_weight)
}

data class MacroRecommendation(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val explanation: String
)

data class DailyEntry(
    val date: String,
    val consumedCalories: Int,
    val consumedProtein: Int,
    val consumedCarbs: Int,
    val consumedFat: Int,
    val recommendedCalories: Int,
    val recommendedProtein: Int,
    val recommendedCarbs: Int,
    val recommendedFat: Int
)

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(
        val userProfile: UserProfile,
        val macroRecommendation: MacroRecommendation? = null,
        val dailyHistory: List<DailyEntry> = emptyList(),
        val isGeneratingRecommendation: Boolean = false
    ) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val claudeService: ClaudeService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadProfile()
    }
    
    private fun loadProfile() {
        viewModelScope.launch {
            try {
                val profile = profileRepository.getProfile()
                var history = profileRepository.getDailyHistory()
                
                val todayStr = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())

                // If no history for past days (ignoring today), populate examples
                if (history.filter { it.date != todayStr }.isEmpty()) {
                    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                    val calendar = java.util.Calendar.getInstance()
                    val sampleList = mutableListOf<DailyEntry>()

                    // Fixed historical samples provided by UI for visualisation
                    val dates = listOf("2025-06-12","2025-06-11","2025-06-10","2025-06-09","2025-06-08")
                    val caloriesList = listOf(2150, 2200, 2050, 2300, 2100)
                    val proteinList = listOf(110, 120, 100, 130, 105)
                    val fatList = listOf(75, 70, 80, 78, 72)
                    val carbsList = listOf(240, 250, 220, 260, 230)

                    dates.forEachIndexed { idx, dateStr ->
                        val cal = caloriesList[idx]
                        val prot = proteinList[idx]
                        val fat = fatList[idx]
                        val carbs = carbsList[idx]
                        sampleList.add(
                            DailyEntry(
                                date = dateStr,
                                consumedCalories = cal,
                                consumedProtein = prot,
                                consumedCarbs = carbs,
                                consumedFat = fat,
                                recommendedCalories = 2000,
                                recommendedProtein = 100,
                                recommendedCarbs = 250,
                                recommendedFat = 67
                            )
                        )
                    }

                    // Generate random entries for remaining days up to 14
                    for (daysAgo in 1..14) {
                        calendar.time = java.util.Date()
                        calendar.add(java.util.Calendar.DATE, -daysAgo)
                        val dateStr = sdf.format(calendar.time)
                        if (sampleList.none { it.date == dateStr }) {
                            sampleList.add(
                                DailyEntry(
                                    date = dateStr,
                                    consumedCalories = (1500..2500).random(),
                                    consumedProtein = (60..150).random(),
                                    consumedCarbs = (150..350).random(),
                                    consumedFat = (40..100).random(),
                                    recommendedCalories = 2000,
                                    recommendedProtein = 100,
                                    recommendedCarbs = 250,
                                    recommendedFat = 67
                                )
                            )
                        }
                    }

                    sampleList.forEach { profileRepository.addDailyEntry(it) }
                    history = profileRepository.getDailyHistory()
                }
                
                val recommendation = profileRepository.getMacroRecommendation()
                
                _uiState.value = ProfileUiState.Success(
                    userProfile = profile,
                    macroRecommendation = recommendation,
                    dailyHistory = history
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al cargar el perfil: ${e.message}")
            }
        }
    }
    
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            try {
                profileRepository.saveProfile(profile)
                val currentState = _uiState.value
                if (currentState is ProfileUiState.Success) {
                    _uiState.value = currentState.copy(userProfile = profile)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al guardar el perfil: ${e.message}")
            }
        }
    }
    
    fun generateMacroRecommendation() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is ProfileUiState.Success) {
                _uiState.value = currentState.copy(isGeneratingRecommendation = true)
                
                try {
                    val recommendation = claudeService.generateMacroRecommendation(currentState.userProfile)
                    profileRepository.saveMacroRecommendation(recommendation)
                    
                    _uiState.value = currentState.copy(
                        macroRecommendation = recommendation,
                        isGeneratingRecommendation = false
                    )
                } catch (e: Exception) {
                    _uiState.value = currentState.copy(isGeneratingRecommendation = false)
                    _uiState.value = ProfileUiState.Error("Error al generar recomendación: ${e.message}")
                }
            }
        }
    }
    
    fun addDailyEntry(entry: DailyEntry) {
        viewModelScope.launch {
            try {
                profileRepository.addDailyEntry(entry)
                val currentState = _uiState.value
                if (currentState is ProfileUiState.Success) {
                    val updatedHistory = currentState.dailyHistory + entry
                    _uiState.value = currentState.copy(dailyHistory = updatedHistory)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error al agregar entrada diaria: ${e.message}")
            }
        }
    }
}

