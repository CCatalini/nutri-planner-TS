package com.austral.nutri_planner_ts.api.manager

import com.austral.nutri_planner_ts.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Attach apiKey as query parameter in case the backend ignores header-based auth
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.SPOONACULAR_API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            // keep the header version for maximum compatibility
            .addHeader("x-api-key", BuildConfig.SPOONACULAR_API_KEY)
            .url(newUrl)
            .build()

        if (com.austral.nutri_planner_ts.BuildConfig.DEBUG) {
            val preview = if (BuildConfig.SPOONACULAR_API_KEY.isNotBlank()) {
                BuildConfig.SPOONACULAR_API_KEY.take(4) + "***"
            } else {
                "<empty>"
            }
            android.util.Log.d("HeaderInterceptor", "ApiKey preview: $preview")
        }

        return chain.proceed(newRequest)
    }
}
