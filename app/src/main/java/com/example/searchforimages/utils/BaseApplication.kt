package com.example.searchforimages.utils

import android.app.Application
import com.example.searchforimages.dagger.component.DaggerApplicationGraph

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationGraph.builder().build()
    }
}