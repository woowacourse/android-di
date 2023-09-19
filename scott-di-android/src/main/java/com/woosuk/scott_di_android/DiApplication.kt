package com.woosuk.scott_di_android

import android.app.Application
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

open class DiApplication(private val newModule: Module) : Application() {

    override fun onCreate() {
        super.onCreate()
        module = newModule
        loadSingletonDependency()
    }

    private fun loadSingletonDependency() {
        val singletonFunctions =
            module::class.declaredMemberFunctions.filter { it.hasAnnotation<Singleton>() }

        singletonFunctions.forEach { function ->
            val dependency =
                Dependency(module, function)
            DiContainer.addDependency(dependency)
        }
    }


    companion object {
        lateinit var module: Module
    }
}
