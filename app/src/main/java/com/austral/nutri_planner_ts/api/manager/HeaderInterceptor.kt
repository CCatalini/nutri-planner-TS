package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-api-key", BuildConfig.SPOONACULAR_API_KEY)
            .build()
        return chain.proceed(request)
    }
}
