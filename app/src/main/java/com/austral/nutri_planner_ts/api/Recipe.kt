package com.austral.nutri_planner_ts.api

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
    val name: String,
    val amount: Double,
    val unit: String,
    val unitShort: String,
    val unitLong: String,
    val possibleUnits: List<String>,
    val estimatedCost: EstimatedCost,
    val consistency: String,
    val aisle: String,
    val image: String,
    val meta: List<String>,
    val nutrition: IngredientNutrition?
)

data class EstimatedCost(
    val value: Double,
    val unit: String
)

data class IngredientNutrition(
    val nutrients: List<Nutrient>,
    val properties: List<NutrientProperty>,
    val caloricBreakdown: CaloricBreakdown,
    val weightPerServing: WeightPerServing
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double
)

data class NutrientProperty(
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

