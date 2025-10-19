package com.example.di.scope

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.di.DependencyInjector
import com.example.di.DependencyKey
import com.example.di.RequireInjection
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

object ViewModelScopeHandler : ScopeHandler {
    override val scopeAnnotation = ViewModelScope::class
    private val instances = mutableMapOf<Any, MutableMap<KClass<*>, Any>>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>?,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
    ): T {
        val instance =
            DependencyInjector.createInstance(
                kClass,
                savedStateHandle,
                DependencyKey(kClass),
            )

        kClass.members
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .forEach { property ->
                val requireInjection = property.findAnnotation<RequireInjection>()
                if (requireInjection?.scope == scopeAnnotation) {
                    property.isAccessible = true
                    val dependencyInstance = property.get(instance)
                    instances.getOrPut(dependencyInstance) { mutableMapOf() }
                }
            }

        Log.d("TAG", "ViewModelScopeHandler : $instances")

        return instance
    }

    fun removeInstance(instance: Any) {
        instance::class
            .members
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .forEach { property ->
                val requireInjection = property.findAnnotation<RequireInjection>()
                if (requireInjection?.scope == scopeAnnotation) {
                    property.isAccessible = true
                    val dependencyInstance = property.get(instance)
                    instances.remove(dependencyInstance)
                }
            }
        Log.d("TAG", "ViewModelScopeHandler 제거: $instances")
    }
}
