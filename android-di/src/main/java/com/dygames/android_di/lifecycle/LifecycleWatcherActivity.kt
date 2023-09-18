package com.dygames.android_di.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dygames.di.DependencyInjector.createDependencies
import com.dygames.di.DependencyInjector.deleteDependencies
import com.dygames.di.DependencyInjector.inject
import com.dygames.di.lifecycle.LifecycleWatcher
import kotlin.reflect.KType

abstract class LifecycleWatcherActivity(val lifecycle: KType) : AppCompatActivity() {
    private val lifecycleWatcher: LifecycleWatcher = object : LifecycleWatcher {
        override fun createDependencies() {
            createDependencies(lifecycle)
        }

        override fun destroyDependencies() {
            deleteDependencies(lifecycle)
        }
    }

    inline fun <reified T : Any> inject(): T {
        return inject<T>(lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleWatcher.createDependencies()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleWatcher.destroyDependencies()
    }
}
