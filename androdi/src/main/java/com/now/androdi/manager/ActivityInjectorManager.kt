package com.now.androdi.manager

import com.now.di.Injector

class ActivityInjectorManager {
    private val savedInjectors = mutableMapOf<String, Injector>()

    fun saveInjector(key: String, injector: Injector) {
        savedInjectors[key] = injector
    }

    fun getInjector(key: String): Injector? {
        return savedInjectors[key]
    }

    fun removeInjector(key: String) {
        savedInjectors.remove(key)
    }
}
