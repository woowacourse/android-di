package com.example.yennydi.application

import android.app.Application
import com.example.yennydi.di.DependencyProvider
import com.example.yennydi.di.Injector

abstract class DiApplication : Application() {
    lateinit var injector: Injector

    lateinit var instanceContainer: ApplicationInstanceContainer

    abstract val dependencyProvider: DependencyProvider

    override fun onCreate() {
        super.onCreate()
        instanceContainer = ApplicationInstanceContainer()
        dependencyProvider.register(instanceContainer)

        injector =
            Injector(instanceContainer).apply {
                instantiateInstances(instanceContainer.getDeferred(), instanceContainer)
            }
    }

    override fun onTerminate() {
        instanceContainer.clear()
        super.onTerminate()
    }
}
