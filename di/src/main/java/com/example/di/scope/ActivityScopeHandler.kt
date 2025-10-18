package com.example.di.scope

import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import com.example.di.DependencyInjector
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object ActivityScopeHandler : BaseScopeHandler() {
    override val scopeAnnotation: KClass<out Annotation> = ActivityScope::class
    private val instances = mutableMapOf<Any, MutableMap<KClass<*>, Any>>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
    ): T {
        val activity = context as ComponentActivity
        val container = instances.getOrPut(activity) { mutableMapOf() }
        activity.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
                    super.onDestroy(owner)
                    instances.remove(activity)
                }
            },
        )
        return container.getOrPut(kClass) {
            DependencyInjector.createInstance(kClass, savedStateHandle, DependencyKey(kClass, null))
        } as T
    }
}
