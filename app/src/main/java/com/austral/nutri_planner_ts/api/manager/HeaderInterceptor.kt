package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-app-id", BuildConfig.NUTRITIONIX_APP_ID)
            .addHeader("x-app-key", BuildConfig.NUTRITIONIX_APP_KEY)
            .build()
        return chain.proceed(request)
    }
}
