package com.dygames.android_di.lifecycle

import android.app.Application
import com.dygames.di.DependencyInjector
import com.dygames.di.lifecycle.LifecycleWatcher
import kotlin.reflect.KType

abstract class LifecycleWatcherApplication(val lifecycle: KType) : Application() {
    private val lifecycleWatcher: LifecycleWatcher = object : LifecycleWatcher {
        override fun createDependencies() {
            DependencyInjector.createDependencies(lifecycle)
        }

        override fun destroyDependencies() {
            TODO("Not yet implemented")
        }
    }

    inline fun <reified T : Any> inject(): T {
        return DependencyInjector.inject(lifecycle) as T
    }

    override fun onCreate() {
        super.onCreate()
        lifecycleWatcher.createDependencies()
    }

    override fun onTerminate() {
        super.onTerminate()
        lifecycleWatcher.destroyDependencies()
    }
}
