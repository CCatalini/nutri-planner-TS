package com.austral.nutri_planner_ts.ui.screens.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.austral.nutri_planner_ts.data.DataStoreManager

@Singleton
class ProfileRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager
) {
    
    private val _profile = MutableStateFlow(UserProfile())
    private val _macroRecommendation = MutableStateFlow<MacroRecommendation?>(null)
    private val _dailyHistory = MutableStateFlow(listOf<DailyEntry>())
    
    suspend fun getProfile(): UserProfile {
        val stored = withContext(Dispatchers.IO) { dataStoreManager.getUserProfile() }
        if (stored != null) _profile.value = stored
        return _profile.value
    }
    
    suspend fun getMacroRecommendation(): MacroRecommendation? {
        val stored = withContext(Dispatchers.IO) { dataStoreManager.getMacroRecommendation() }
        if (stored != null) _macroRecommendation.value = stored
        return _macroRecommendation.value
    }
    
    suspend fun getDailyHistory(): List<DailyEntry> {
        val stored = withContext(Dispatchers.IO) { dataStoreManager.getDailyHistory() }
        if (stored.isNotEmpty()) _dailyHistory.value = stored
        return _dailyHistory.value
    }
    
    suspend fun saveProfile(profile: UserProfile) {
        _profile.value = profile
        withContext(Dispatchers.IO) { dataStoreManager.saveUserProfile(profile) }
    }
    
    suspend fun saveMacroRecommendation(recommendation: MacroRecommendation) {
        _macroRecommendation.value = recommendation
        withContext(Dispatchers.IO) { dataStoreManager.saveMacroRecommendation(recommendation) }
    }
    
    suspend fun addDailyEntry(entry: DailyEntry) {
        val currentHistory = _dailyHistory.value.toMutableList()
        val existingEntryIndex = currentHistory.indexOfFirst { it.date == entry.date }
        
        if (existingEntryIndex >= 0) {
            currentHistory[existingEntryIndex] = entry
        } else {
            currentHistory.add(entry)
        }
        
        _dailyHistory.value = currentHistory.sortedByDescending { it.date }
        
        // Persist updated history
        withContext(Dispatchers.IO) { dataStoreManager.saveDailyHistory(_dailyHistory.value) }
    }
    
    fun getRecommendedMacros(): MacroRecommendation? {
        return _macroRecommendation.value
    }
} 