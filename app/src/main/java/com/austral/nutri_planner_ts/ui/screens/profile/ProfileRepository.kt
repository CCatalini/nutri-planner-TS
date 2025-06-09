package com.austral.nutri_planner_ts.ui.screens.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {
    
    private val _profile = MutableStateFlow(UserProfile())
    private val _macroRecommendation = MutableStateFlow<MacroRecommendation?>(null)
    private val _dailyHistory = MutableStateFlow(listOf<DailyEntry>())
    
    suspend fun getProfile(): UserProfile = _profile.value
    
    suspend fun getMacroRecommendation(): MacroRecommendation? = _macroRecommendation.value
    
    suspend fun getDailyHistory(): List<DailyEntry> = _dailyHistory.value
    
    suspend fun saveProfile(profile: UserProfile) {
        _profile.value = profile
    }
    
    suspend fun saveMacroRecommendation(recommendation: MacroRecommendation) {
        _macroRecommendation.value = recommendation
    }
    
    suspend fun addDailyEntry(entry: DailyEntry) {
        val currentHistory = _dailyHistory.value.toMutableList()
        val existingEntryIndex = currentHistory.indexOfFirst { it.date == entry.date }
        
        if (existingEntryIndex >= 0) {
            // Update existing entry
            currentHistory[existingEntryIndex] = entry
        } else {
            // Add new entry
            currentHistory.add(entry)
        }
        
        _dailyHistory.value = currentHistory.sortedByDescending { it.date }
    }
    
    fun getRecommendedMacros(): MacroRecommendation? {
        return _macroRecommendation.value
    }
} 