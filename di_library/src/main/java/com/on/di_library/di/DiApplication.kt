package com.on.di_library.di

import android.app.Application

abstract class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DiContainer.setContext(this)
        DiContainer.getAnnotatedModules()
    }
}