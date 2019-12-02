package com.example.searchforimages.repository

import com.example.searchforimages.connection.api.GalleryServices
import com.example.searchforimages.model.Response
import io.reactivex.Maybe
import javax.inject.Inject

class ImageRepository @Inject constructor(private val galleryServices: GalleryServices) {

    fun getImagePosts(page: Int, searchQuery: String): Maybe<Response> {
        return galleryServices.requestImagePosts(page, searchQuery)
    }
}