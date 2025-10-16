package com.example.di.wrapper

import android.app.Application
import com.example.di.AppContainer

abstract class DIApplication : Application() {
    val container: AppContainer = AppContainer()

    override fun onCreate() {
        super.onCreate()

        container.init(this)
        registerActivityLifecycleCallbacks(ActivityFieldInjector(container))
    }
}
