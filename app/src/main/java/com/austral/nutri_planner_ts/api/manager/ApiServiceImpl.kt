package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.api.IngredientSearchResult
import com.austral.nutri_planner_ts.api.IngredientSearchResponse
import com.austral.nutri_planner_ts.api.IngredientInformation
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiServiceImpl @Inject constructor() {

    fun searchIngredients(
        baseUrl: String = "https://api.spoonacular.com/",
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

        val service: ApiService = retrofit.create(ApiService::class.java)
        val call = service.searchIngredients(query)

        call.enqueue(object : Callback<IngredientSearchResponse> {
            override fun onResponse(
                call: Call<IngredientSearchResponse>,
                response: Response<IngredientSearchResponse>
            ) {
                loadingFinished()
                if (response.isSuccessful) {
                    response.body()?.results?.let(onSuccess) ?: onFail()
                } else {
                    onFail()
                }
            }

            override fun onFailure(call: Call<IngredientSearchResponse>, t: Throwable) {
                onFail()
                loadingFinished()
            }
        })
    }

    fun getIngredientInformation(
        baseUrl: String = "https://api.spoonacular.com/",
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

        val service: ApiService = retrofit.create(ApiService::class.java)
        val call = service.getIngredientInformation(ingredientId, amount, unit)

        call.enqueue(object : Callback<IngredientInformation> {
            override fun onResponse(
                call: Call<IngredientInformation>,
                response: Response<IngredientInformation>
            ) {
                loadingFinished()
                if (response.isSuccessful) {
                    response.body()?.let(onSuccess) ?: onFail()
                } else {
                    onFail()
                }
            }

            override fun onFailure(call: Call<IngredientInformation>, t: Throwable) {
                onFail()
                loadingFinished()
            }
        })
    }
}
