package com.austral.nutri_planner_ts.api.manager

import android.content.Context
import android.widget.Toast
import com.austral.nutri_planner_ts.api.CommonFood
import com.austral.nutri_planner_ts.api.RecipeResponse
import retrofit.Call
import retrofit.Callback
import retrofit.GsonConverterFactory
import retrofit.Response
import retrofit.Retrofit
import javax.inject.Inject

class ApiServiceImpl @Inject constructor() {

    fun getRecipe(context: Context, onSuccess: (List<CommonFood>) -> Unit, onFail: () -> Unit, loadingFinished: () -> Unit) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://trackapi.nutritionix.com")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

        val service: ApiService = retrofit.create(ApiService::class.java)

        val call: Call<RecipeResponse> = service.getRecipe("apple")

        call.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(response: Response<RecipeResponse>?, retrofit: Retrofit?) {
                loadingFinished()
                if(response?.isSuccess == true) {
                    val recipeResponse: RecipeResponse? = response.body()
                    recipeResponse?.common?.let { commonFoods ->
                        onSuccess(commonFoods)
                    } ?: run {
                        onFailure(Exception("Empty response"))
                    }
                } else {
                    onFailure(Exception("Bad request"))
                }
            }

            override fun onFailure(t: Throwable?) {
                Toast.makeText(context, "Can't get comiditas", Toast.LENGTH_SHORT).show()
                onFail()
                loadingFinished()
            }
        })
    }
}