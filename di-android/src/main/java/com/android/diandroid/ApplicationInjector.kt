package com.android.diandroid

import android.app.Application
import com.android.di.component.DiContainer
import com.android.di.component.DiInjector
import com.android.di.component.Module

abstract class ApplicationInjector : Application() {
    private lateinit var diInjector: DiInjector
    private val childInjectors = mutableMapOf<String, DiInjector>()

    override fun onCreate() {
        super.onCreate()
        diInjector = DiInjector(DiContainer())
    }

    fun injectModule(module: Module) {
        diInjector.addModule(module)
    }

    fun getApplicationContainer(): DiContainer {
        return diInjector.diContainer
    }

    fun saveInjector(
        key: String,
        injector: DiInjector,
    ) {
        childInjectors[key] = injector
    }

    fun getInjector(key: String): DiInjector? {
        return childInjectors[key]
    }

    fun removeInjector(key: String) {
        childInjectors.remove(key)
    }
}
