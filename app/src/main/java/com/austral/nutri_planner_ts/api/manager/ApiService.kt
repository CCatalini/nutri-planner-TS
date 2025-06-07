package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.api.IngredientSearchResponse
import com.austral.nutri_planner_ts.api.IngredientInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("food/ingredients/search")
    fun searchIngredients(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("sortDirection") sortDirection: String = "desc"
    ): Call<IngredientSearchResponse>

    @GET("food/ingredients/{id}/information")
    fun getIngredientInformation(
        @Path("id") id: Int,
        @Query("amount") amount: Double = 100.0,
        @Query("unit") unit: String = "grams"
    ): Call<IngredientInformation>
}
