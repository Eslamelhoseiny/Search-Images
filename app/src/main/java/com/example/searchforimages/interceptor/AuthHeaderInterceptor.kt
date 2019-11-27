package com.example.searchforimages.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val ongoing = chain.request().newBuilder()
        ongoing.header("Authorization", "Client-ID 37398025a821c1b")
        return chain.proceed(ongoing.build())
    }

}