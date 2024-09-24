package com.example.di

import android.app.Application
import com.example.di.annotation.LifeCycleScope

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
        DIInjector.injectModule(module, LifeCycleScope.APPLICATION)

        val targetApplication = this::class.java.cast(this)
        DIInjector.injectFields(targetApplication)
    }

    private fun releaseDependencies() {
        DIInjector.releaseModule(module::class, LifeCycleScope.APPLICATION)
    }
}
