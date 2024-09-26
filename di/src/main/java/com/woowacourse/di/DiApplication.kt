package com.woowacourse.di

import android.app.Application

abstract class DiApplication : Application() {
    private lateinit var diInjector: DiInjector
    private val childInjectors = mutableMapOf<String, DiInjector>()

    override fun onCreate() {
        super.onCreate()
        diInjector = DiInjector(DiContainer())
    }

    fun injectModule(diModule: DiModule) {
        diInjector.addModule(diModule)
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
