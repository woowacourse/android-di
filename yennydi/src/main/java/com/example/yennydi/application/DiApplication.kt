package com.example.yennydi.application

import android.app.Application
import com.example.di.DependencyModule
import com.example.di.Injector

abstract class DiApplication : Application() {
    lateinit var injector: Injector

    lateinit var instanceModule: ApplicationInstanceModule

    override fun onCreate() {
        super.onCreate()
        setupDependency()
        instanceModule = ApplicationInstanceModule(DependencyModule.getSingletonInstances())
        injector =
            Injector(instanceModule).apply {
                instantiateInstances(DependencyModule.getSingletonDependencies(), instanceModule)
            }
    }

    abstract fun setupDependency()

    override fun onTerminate() {
        instanceModule.clear()
        super.onTerminate()
    }
}
