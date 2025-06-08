package com.austral.nutri_planner_ts.api

// Data classes for recipe search
data class RecipeSearchResponse(
    val results: List<RecipeSearchResult>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

data class RecipeSearchResult(
    val id: Int,
    val title: String,
    val image: String?
)

// Spoonacular Ingredient Search Response
data class IngredientSearchResponse(
    val results: List<IngredientSearchResult>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

data class IngredientSearchResult(
    val id: Int,
    val name: String,
    val image: String
)

// Spoonacular Ingredient Information with Nutrition
data class IngredientInformation(
    val id: Int,
    val original: String,
    val originalName: String,
    val name: String,
    val amount: Double,
    val unit: String,
    val unitShort: String,
    val unitLong: String,
    val possibleUnits: List<String>,
    val estimatedCost: EstimatedCost?,
    val consistency: String,
    val shoppingListUnits: List<String>?,
    val aisle: String,
    val image: String,
    val meta: List<String>,
    val nutrition: Nutrition?
)

data class EstimatedCost(
    val value: Double,
    val unit: String
)

data class Nutrition(
    val nutrients: List<Nutrient>,
    val properties: List<NutritionProperty>,
    val flavonoids: List<Flavonoid>,
    val caloricBreakdown: CaloricBreakdown,
    val weightPerServing: WeightPerServing
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double
)

data class NutritionProperty(
    val name: String,
    val amount: Double,
    val unit: String
)

data class Flavonoid(
    val name: String,
    val amount: Double,
    val unit: String
)

data class CaloricBreakdown(
    val percentProtein: Double,
    val percentFat: Double,
    val percentCarbs: Double
)

data class WeightPerServing(
    val amount: Double,
    val unit: String
)

// Detailed recipe information with nutrition
data class RecipeInformation(
    val id: Int,
    val title: String,
    val image: String?,
    val servings: Int,
    val readyInMinutes: Int,
    val summary: String?,
    val instructions: String?,
    val nutrition: RecipeNutrition?
)

data class RecipeNutrition(
    val nutrients: List<RecipeNutrient>
)

data class RecipeNutrient(
    val name: String,
    val amount: Double,
    val unit: String
)

