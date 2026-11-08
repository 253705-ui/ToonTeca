package com.lexziewyn.toonteca.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String = "token_bearer_demo") : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}