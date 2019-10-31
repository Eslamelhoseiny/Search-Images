package com.example.searchforimages.view_model

import com.example.searchforimages.utils.Status

data class SearchActivityStatus(val status: Status, val error: Throwable? = null) {

    companion object {

        fun loading(): SearchActivityStatus {
            return SearchActivityStatus(Status.LOADING)
        }

        fun complete(): SearchActivityStatus {
            return SearchActivityStatus(Status.COMPLETE)
        }

        fun success(): SearchActivityStatus {
            return SearchActivityStatus(Status.SUCCESS)
        }

        fun tooShort(): SearchActivityStatus {
            return SearchActivityStatus(Status.TOO_SHORT)
        }

        fun error(error: Throwable): SearchActivityStatus {
            return SearchActivityStatus(Status.ERROR, error)
        }

    }

}