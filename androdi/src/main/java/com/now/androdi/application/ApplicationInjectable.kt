package com.now.androdi.application

import android.app.Application
import com.now.di.Container
import com.now.di.Injector
import com.now.di.Module

abstract class ApplicationInjectable : Application() {
    lateinit var injector: Injector

    override fun onCreate() {
        super.onCreate()
        injector = Injector(Container(null))
    }

    fun injectModule(module: Module) {
        injector.addModule(module)
    }
}
