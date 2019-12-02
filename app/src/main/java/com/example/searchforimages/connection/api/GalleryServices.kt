package com.example.searchforimages.connection.api

import com.example.searchforimages.model.Response
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GalleryServices {

    @GET("gallery/search/time/{page}")
    fun requestImagePosts(@Path("page") page: Int, @Query("q") searchQuery: String): Maybe<Response>
}