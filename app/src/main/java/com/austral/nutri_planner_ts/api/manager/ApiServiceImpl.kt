package com.austral.nutri_planner_ts.api.manager

import android.content.Context
import android.widget.Toast
import com.austral.nutri_planner_ts.R
import com.austral.nutri_planner_ts.api.CommonFood
import com.austral.nutri_planner_ts.api.RecipeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import okhttp3.OkHttpClient

class ApiServiceImpl @Inject constructor() {

    fun getRecipe(
        context: Context,
        onSuccess: (List<CommonFood>) -> Unit,
        onFail: () -> Unit,
        loadingFinished: () -> Unit
    ) {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(context.getString(R.string.nutritionix_url))
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
                    val recipeResponse = response.body()
                    recipeResponse?.common?.let { commonFoods ->
                        onSuccess(commonFoods)
                    } ?: run {
                        onFail()
                    }
                } else {
                    onFail()
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Toast.makeText(context, "Can't get comiditas", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}
