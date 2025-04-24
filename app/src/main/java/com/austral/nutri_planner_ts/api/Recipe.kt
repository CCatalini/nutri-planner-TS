package com.austral.nutri_planner_ts.api


data class CommonFood(
    val common_type: String?,
    val tag_name: String,
    val serving_qty: Int,
    val food_name: String,
    val serving_unit: String,
    val tag_id: String,
)

data class RecipeResponse(
    val common: List<CommonFood>
)
