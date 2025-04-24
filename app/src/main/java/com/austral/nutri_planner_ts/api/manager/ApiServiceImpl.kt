package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.api.CommonFood
import com.austral.nutri_planner_ts.api.RecipeResponse
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiServiceImpl @Inject constructor() {

    fun getRecipe(
        baseUrl: String,
        onSuccess: (List<CommonFood>) -> Unit,
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
        val call = service.getRecipe("apple")

        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                loadingFinished()
                if (response.isSuccessful) {
                    response.body()?.common?.let(onSuccess) ?: onFail()
                } else {
                    onFail()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                onFail()
                loadingFinished()
            }
        })
    }
}
