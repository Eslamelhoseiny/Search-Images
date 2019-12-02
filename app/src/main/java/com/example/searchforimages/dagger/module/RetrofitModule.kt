package com.example.searchforimages.dagger.module

import com.example.searchforimages.BuildConfig
import com.example.searchforimages.connection.api.GalleryServices
import com.example.searchforimages.connection.interceptor.AuthHeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {
    companion object {
        const val BASE_URL = "https://api.imgur.com/3/"
        const val OK_HTTP_TIMEOUT = 15L
    }

    @Provides
    @Singleton
    internal fun provideAuthHeaderInterceptor(): AuthHeaderInterceptor {
        return AuthHeaderInterceptor()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(interceptor: AuthHeaderInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideGalleryService(retrofit: Retrofit): GalleryServices {
        return retrofit.create(GalleryServices::class.java)
    }

}