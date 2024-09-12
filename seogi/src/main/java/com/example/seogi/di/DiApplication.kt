package com.example.seogi.di

import android.app.Application

abstract class DiApplication(private val module: Module) : Application() {
    override fun onCreate() {
        super.onCreate()
        diContainer = DiContainer(module)
    }

    companion object {
        lateinit var diContainer: DiContainer
            private set
    }
}
