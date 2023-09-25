package com.now.androdi.application

import android.app.Application
import com.now.androdi.manager.ActivityInjectorManager
import com.now.di.Container
import com.now.di.Injector
import com.now.di.Module

abstract class ApplicationInjectable : Application() {
    lateinit var injector: Injector
    lateinit var activityInjectorManager: ActivityInjectorManager

    override fun onCreate() {
        super.onCreate()
        injector = Injector(Container(null))
        activityInjectorManager = ActivityInjectorManager()
    }

    fun injectModule(module: Module) {
        injector.addModule(module)
    }
}
