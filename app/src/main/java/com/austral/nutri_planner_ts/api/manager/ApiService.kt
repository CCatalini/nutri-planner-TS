package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.api.RecipeResponse
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Headers
import retrofit.http.Query

interface ApiService {

    @Headers(
        "x-app-id: a56d1862",
        "x-app-key: 64f01ce1d766a23e47443cb0f449d881"
    )
    @GET("/v2/search/instant")
    fun getRecipe(
        @Query("query") query: String,
    ): Call<RecipeResponse>
}
