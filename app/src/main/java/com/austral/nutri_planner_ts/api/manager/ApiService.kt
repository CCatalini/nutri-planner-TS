package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.api.RecipeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/v2/search/instant")
    fun getRecipe(@Query("query") query: String): Call<RecipeResponse>
}
