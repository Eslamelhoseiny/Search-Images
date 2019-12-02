package com.example.searchforimages.dagger.component

import com.example.searchforimages.dagger.module.RetrofitModule
import com.example.searchforimages.repository.ImageRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface ApplicationGraph {
    fun getImageRepository(): ImageRepository
}