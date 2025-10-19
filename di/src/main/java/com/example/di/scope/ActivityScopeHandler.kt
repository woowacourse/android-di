package com.example.di.scope

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object ActivityScopeHandler : ScopeHandler {
    override val scopeAnnotation: KClass<out Annotation> = ActivityScope::class
    private val cache = mutableMapOf<DependencyKey<*>, Any>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getOrCreate(
        key: DependencyKey<T>,
        activity: ComponentActivity?,
        factory: () -> T,
    ): T {
        activity?.let { addLifecycleObserver(it, key) }
        return cache.getOrPut(key) { factory() } as T
    }

    private fun addLifecycleObserver(
        activity: ComponentActivity,
        key: DependencyKey<*>,
    ) {
        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    cache.remove(key)
                }
            },
        )
    }
}
