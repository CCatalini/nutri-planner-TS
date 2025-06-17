package com.austral.nutri_planner_ts.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.austral.nutri_planner_ts.api.IngredientInformation
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.ui.screens.profile.DailyEntry
import com.austral.nutri_planner_ts.ui.screens.profile.MacroRecommendation
import com.austral.nutri_planner_ts.ui.screens.profile.UserProfile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

// Provide DataStore instance via property delegate on Context
private val Context.dataStore by preferencesDataStore(name = "nutriplanner_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val gson = Gson()

    // Preference keys
    private object Keys {
        val MEALS_DATE = stringPreferencesKey("meals_date")
        val MEALS_JSON = stringPreferencesKey("meals_json")
        val NUTRITION_JSON = stringPreferencesKey("nutrition_json")

        val PROFILE_JSON = stringPreferencesKey("profile_json")
        val MACRO_RECOMMEND_JSON = stringPreferencesKey("macro_recommend_json")
        val DAILY_HISTORY_JSON = stringPreferencesKey("daily_history_json")
    }

    /* ---------- Meals per day ---------- */

    suspend fun saveMeals(
        date: String,
        meals: List<IngredientSearchResult>,
        nutrition: List<IngredientInformation>
    ) {
        val mealsJson = gson.toJson(meals)
        val nutritionJson = gson.toJson(nutrition)
        context.dataStore.edit { prefs ->
            prefs[Keys.MEALS_DATE] = date
            prefs[Keys.MEALS_JSON] = mealsJson
            prefs[Keys.NUTRITION_JSON] = nutritionJson
        }
    }

    suspend fun getMealsForDate(date: String): Pair<List<IngredientSearchResult>, List<IngredientInformation>> {
        val prefs: Preferences = context.dataStore.data.first()
        val storedDate = prefs[Keys.MEALS_DATE]
        val mealsJson = prefs[Keys.MEALS_JSON]
        val nutritionJson = prefs[Keys.NUTRITION_JSON]

        return if (storedDate == date && mealsJson != null && nutritionJson != null) {
            val mealsType = object : TypeToken<List<IngredientSearchResult>>() {}.type
            val nutritionType = object : TypeToken<List<IngredientInformation>>() {}.type

            val meals: List<IngredientSearchResult> = gson.fromJson(mealsJson, mealsType) ?: emptyList()
            val nutrition: List<IngredientInformation> = gson.fromJson(nutritionJson, nutritionType) ?: emptyList()
            Pair(meals, nutrition)
        } else {
            Pair(emptyList(), emptyList())
        }
    }

    /* ---------- User profile ---------- */

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.PROFILE_JSON] = gson.toJson(profile)
        }
    }

    suspend fun getUserProfile(): UserProfile? {
        val prefs: Preferences = context.dataStore.data.first()
        val json = prefs[Keys.PROFILE_JSON] ?: return null
        return try {
            gson.fromJson(json, UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /* ---------- Macro recommendation ---------- */

    suspend fun saveMacroRecommendation(rec: MacroRecommendation) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MACRO_RECOMMEND_JSON] = gson.toJson(rec)
        }
    }

    suspend fun getMacroRecommendation(): MacroRecommendation? {
        val prefs: Preferences = context.dataStore.data.first()
        val json = prefs[Keys.MACRO_RECOMMEND_JSON] ?: return null
        return try {
            gson.fromJson(json, MacroRecommendation::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /* ---------- Daily history ---------- */

    suspend fun saveDailyHistory(history: List<DailyEntry>) {
        context.dataStore.edit { prefs ->
            prefs[Keys.DAILY_HISTORY_JSON] = gson.toJson(history)
        }
    }

    suspend fun getDailyHistory(): List<DailyEntry> {
        val prefs: Preferences = context.dataStore.data.first()
        val json = prefs[Keys.DAILY_HISTORY_JSON]
        return if (json.isNullOrEmpty()) emptyList() else try {
            val type = object : TypeToken<List<DailyEntry>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
} 