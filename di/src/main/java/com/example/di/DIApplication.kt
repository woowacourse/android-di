package com.example.di

import android.app.Application

abstract class DIApplication : Application() {
    abstract val module: DIModule

    override fun onCreate() {
        super.onCreate()

        injectDependencies()
    }

    override fun onTerminate() {
        super.onTerminate()

        releaseDependencies()
    }

    private fun injectDependencies() {
        DIInjector.injectModule(module)

        val targetApplication = this::class.java.cast(this)
        DIInjector.injectFields(targetApplication)
    }

    private fun releaseDependencies() {
        DIInjector.releaseModule(module)
    }
}