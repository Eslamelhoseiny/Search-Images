package com.example.searchforimages.model

import com.example.searchforimages.model.ImagePost
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response(
        @SerializedName("data")
        @Expose
        val data: List<ImagePost>? = null
)