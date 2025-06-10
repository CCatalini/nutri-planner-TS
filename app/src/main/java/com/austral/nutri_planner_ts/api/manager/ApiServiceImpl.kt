package com.austral.nutri_planner_ts.api.manager

import android.util.Log
import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.IngredientSearchResponse
import com.austral.nutri_planner_ts.api.IngredientInformation
import com.austral.nutri_planner_ts.api.RecipeSearchResponse
import com.austral.nutri_planner_ts.ui.theme.ApiConstants
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiServiceImpl @Inject constructor() {

    // Error callback with specific error types
    data class ApiError(
        val code: Int,
        val message: String,
        val isQuotaExceeded: Boolean = false,
        val isNetworkError: Boolean = false
    )

    private fun handleApiError(responseCode: Int, errorBody: String?): ApiError {
        return when (responseCode) {
            402 -> ApiError(
                code = responseCode, 
                message = "API quota exceeded - daily limit reached",
                isQuotaExceeded = true
            )
            401 -> ApiError(
                code = responseCode,
                message = "API key invalid or unauthorized"
            )
            429 -> ApiError(
                code = responseCode,
                message = "Rate limit exceeded"
            )
            else -> ApiError(
                code = responseCode,
                message = "API request failed with code: $responseCode"
            )
        }
    }

    fun searchIngredients(
        baseUrl: String = ApiConstants.SPOONACULAR_BASE_URL,
        query: String = "apple",
        onSuccess: (List<IngredientSearchResult>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.searchIngredients(query = query).enqueue(object : Callback<IngredientSearchResponse> {
            override fun onResponse(
                call: Call<IngredientSearchResponse>,
                response: Response<IngredientSearchResponse>
            ) {
                loadingFinished()
                if (response.isSuccessful) {
                    val ingredients = response.body()?.results ?: emptyList()
                    onSuccess(ingredients)
                } else {
                    onFail()
                }
            }

            override fun onFailure(call: Call<IngredientSearchResponse>, t: Throwable) {
                loadingFinished()
                onFail()
            }
        })
    }

    fun getIngredientInformation(
        baseUrl: String = ApiConstants.SPOONACULAR_BASE_URL,
        ingredientId: Int,
        amount: Double = 100.0,
        unit: String = "grams",
        onSuccess: (IngredientInformation) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.getIngredientInformation(
            id = ingredientId,
            amount = amount,
            unit = unit
        ).enqueue(object : Callback<IngredientInformation> {
            override fun onResponse(
                call: Call<IngredientInformation>,
                response: Response<IngredientInformation>
            ) {
                loadingFinished()
                if (response.isSuccessful) {
                    val ingredientInfo = response.body()
                    if (ingredientInfo != null) {
                        onSuccess(ingredientInfo)
                    } else {
                        onFail()
                    }
                } else {
                    onFail()
                }
            }

            override fun onFailure(call: Call<IngredientInformation>, t: Throwable) {
                loadingFinished()
                onFail()
            }
        })
    }

    fun searchFood(
        baseUrl: String = ApiConstants.SPOONACULAR_BASE_URL,
        query: String = "chicken",
        onSuccess: (List<IngredientSearchResult>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        Log.d("SearchFood", "Starting unified search for: $query")

        // Search ingredients first
        service.searchIngredients(query = query, number = 5).enqueue(object : Callback<IngredientSearchResponse> {
            override fun onResponse(
                call: Call<IngredientSearchResponse>,
                response: Response<IngredientSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val ingredients = response.body()?.results ?: emptyList()
                    Log.d("SearchFood", "Found ${ingredients.size} ingredients")
                    
                    // Now search recipes and combine results
                    service.searchRecipes(query = query, number = 5, addRecipeInformation = true).enqueue(object : Callback<RecipeSearchResponse> {
                        override fun onResponse(
                            call: Call<RecipeSearchResponse>,
                            response: Response<RecipeSearchResponse>
                        ) {
                            loadingFinished()
                            
                            if (response.isSuccessful) {
                                val recipeResults = response.body()?.results ?: emptyList()
                                Log.d("SearchFood", "Found ${recipeResults.size} recipes")
                                
                                val recipes = recipeResults.map { recipe ->
                                    Log.d("SearchFood", "Recipe: ${recipe.title}")
                                    Log.d("SearchFood", "Recipe image URL: ${recipe.image}")
                                    val finalImageUrl = recipe.image ?: ApiConstants.SPOONACULAR_RECIPE_DEFAULT_IMAGE
                                    Log.d("SearchFood", "Final image URL: $finalImageUrl")
                                    // Convert recipe to IngredientSearchResult for compatibility
                                    IngredientSearchResult(
                                        id = recipe.id,
                                        name = recipe.title,
                                        image = finalImageUrl // Keep full URL for recipes
                                    )
                                }
                                
                                // Combine RECIPES FIRST, then ingredients
                                val combinedResults = recipes + ingredients
                                Log.d("SearchFood", "Total results: ${combinedResults.size} (${recipes.size} recipes, ${ingredients.size} ingredients)")
                                onSuccess(combinedResults)
                            } else {
                                Log.e("SearchFood", "Recipe search failed with code: ${response.code()}")
                                Log.e("SearchFood", "Recipe search error: ${response.errorBody()?.string()}")
                                
                                // Check for specific error codes in recipe search
                                when (response.code()) {
                                    402 -> {
                                        Log.e("SearchFood", "Recipe API quota exceeded - daily limit reached")
                                    }
                                    401 -> {
                                        Log.e("SearchFood", "Recipe API key invalid or unauthorized")
                                    }
                                    429 -> {
                                        Log.e("SearchFood", "Recipe rate limit exceeded")
                                    }
                                }
                                
                                // Return just ingredients if recipe search fails
                                onSuccess(ingredients)
                            }
                        }

                        override fun onFailure(call: Call<RecipeSearchResponse>, t: Throwable) {
                            loadingFinished()
                            Log.e("SearchFood", "Recipe search failed with exception: ${t.message}")
                            // If recipe search fails, return just ingredients
                            onSuccess(ingredients)
                        }
                    })
                } else {
                    loadingFinished()
                    Log.e("SearchFood", "Ingredient search failed with code: ${response.code()}")
                    Log.e("SearchFood", "Response error body: ${response.errorBody()?.string()}")
                    
                    // Check for specific error codes
                    when (response.code()) {
                        402 -> {
                            Log.e("SearchFood", "API quota exceeded - daily limit reached")
                        }
                        401 -> {
                            Log.e("SearchFood", "API key invalid or unauthorized")
                        }
                        429 -> {
                            Log.e("SearchFood", "Rate limit exceeded")
                        }
                        else -> {
                            Log.e("SearchFood", "API request failed with code: ${response.code()}")
                        }
                    }
                    onFail()
                }
            }

            override fun onFailure(call: Call<IngredientSearchResponse>, t: Throwable) {
                loadingFinished()
                Log.e("SearchFood", "Ingredient search failed with exception: ${t.message}")
                onFail()
            }
        })
    }

    private fun extractImageName(imageUrl: String?): String {
        return if (imageUrl != null) {
            // For recipes, images come as full URLs, extract filename
            imageUrl.substringAfterLast("/").substringBefore("-") + ".jpg"
        } else {
            "recipe-placeholder.jpg"
        }
    }

    fun getRecipeInformation(
        baseUrl: String = ApiConstants.SPOONACULAR_BASE_URL,
        recipeId: Int,
        onSuccess: (com.austral.nutri_planner_ts.api.RecipeInformation) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.getRecipeInformation(id = recipeId, includeNutrition = true).enqueue(object : Callback<com.austral.nutri_planner_ts.api.RecipeInformation> {
            override fun onResponse(
                call: Call<com.austral.nutri_planner_ts.api.RecipeInformation>,
                response: Response<com.austral.nutri_planner_ts.api.RecipeInformation>
            ) {
                loadingFinished()
                if (response.isSuccessful) {
                    val recipeInfo = response.body()
                    if (recipeInfo != null) {
                        Log.d("ApiServiceImpl", "Recipe info received: ${recipeInfo.title}")
                        Log.d("ApiServiceImpl", "Recipe nutrition: ${recipeInfo.nutrition?.nutrients?.size ?: 0} nutrients")
                        onSuccess(recipeInfo)
                    } else {
                        onFail()
                    }
                } else {
                    Log.e("ApiServiceImpl", "Recipe info failed with code: ${response.code()}")
                    onFail()
                }
            }

            override fun onFailure(call: Call<com.austral.nutri_planner_ts.api.RecipeInformation>, t: Throwable) {
                loadingFinished()
                Log.e("ApiServiceImpl", "Recipe info failed with exception: ${t.message}")
                onFail()
            }
        })
    }
}
