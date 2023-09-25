package com.hyegyeong.di

import android.app.Application

abstract class DiApplication : Application() {
    abstract val module: DiModule
    override fun onCreate() {
        super.onCreate()
        DiContainer.dependencyModule = module
    }
}
