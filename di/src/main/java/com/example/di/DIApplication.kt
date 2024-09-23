package com.example.di

import android.app.Application
import com.example.di.annotations.ApplicationScope
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation

open class DIApplication(
    private val appModule: AppModule,
) : Application() {
    override fun onCreate() {
        super.onCreate()
        module = appModule
        loadSingletonDependency()
    }

    private fun loadSingletonDependency() {
        val singletonFunctions =
            module::class.declaredMemberFunctions.filter { it.hasAnnotation<ApplicationScope>() }

        singletonFunctions.forEach { function ->
            val dependency =
                Dependency(module, function)
            DIContainer.addDependency(dependency)
        }
    }

    companion object {
        lateinit var module: AppModule
    }
}
