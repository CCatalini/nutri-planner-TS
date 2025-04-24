package com.austral.nutri_planner_ts.api

data class Photo(
    val thumb: String,
    val highres: String? = null,
    val is_user_uploaded: Boolean? = null
)

data class CommonFood(
    val common_type: String?,
    val tag_name: String,
    val serving_qty: Int,
    val photo: Photo?,
    val food_name: String,
    val serving_unit: String,
    val tag_id: String,
    val locale: String
)

data class RecipeResponse(
    val common: List<CommonFood>
)
