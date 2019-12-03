package com.example.searchforimages.connection.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {

    //2758066fa9b683235a4802390cc02bfe9c54287e
    override fun intercept(chain: Interceptor.Chain): Response {
        val ongoing = chain.request().newBuilder()
        ongoing.header("Authorization", "Client-ID 8030ef25d77188c")
        return chain.proceed(ongoing.build())
    }

}