package com.example.di.scope

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.di.DependencyInjector
import com.example.di.DependencyKey
import kotlin.reflect.KClass

object AppScopeHandler : ScopeHandler {
    override val scopeAnnotation: KClass<out Annotation> = AppScope::class
    private val instances = mutableMapOf<DependencyKey<*>, Any>()

    init {
        ScopeContainer.setHandler(scopeAnnotation, this)
    }

    override fun <T : Any> getInstance(
        kClass: KClass<T>,
        qualifier: KClass<out Annotation>?,
        savedStateHandle: SavedStateHandle?,
        context: Any?,
    ): T {
        val key = DependencyKey(kClass, qualifier)
        val instance =
            instances.getOrPut(key) {
                DependencyInjector.createInstance(kClass, savedStateHandle, key)
            }
//        kClass
//            .members
//            .filterIsInstance<KMutableProperty1<Any, Any>>()
//            .forEach { property ->
//                val requireInjection = property.findAnnotation<RequireInjection>()
//                if (requireInjection?.scope == scopeAnnotation) {
//                    property.isAccessible = true
//                    val dependencyInstance = property.get(instance)
//                    instances.getOrPut(dependencyInstance) { mutableMapOf() }
//                }
//            }
        Log.d("TAG", "AppScopeHandler : $instances")

        return instance as T
    }

    fun putInstance(
        kClass: KClass<*>,
        qualifier: KClass<out Annotation>?,
        instance: Any,
    ) {
        val key = DependencyKey(kClass, qualifier)
        instances[key] = instance
    }
}
