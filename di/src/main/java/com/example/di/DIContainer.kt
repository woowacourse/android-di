package com.example.di

import com.example.di.annotation.QualifierType
import kotlin.reflect.KClass

object DIContainer {
    private val instances: MutableMap<Dependency, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
    ): T {
        val dependency = Dependency(type, qualifierType)
        return instances[dependency] as? T ?: DIInjector.createInstance(type)
    }

    fun <T : Any> addInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
        instance: Any,
    ) {
        val dependency = Dependency(type, qualifierType)
        if (!instances.containsKey(dependency)) {
            instances[dependency] = instance
        }
    }

    fun <T : Any> removeInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
    ) {
        val dependency = Dependency(type, qualifierType)
        instances.remove(dependency)
    }

    fun clear() {
        instances.clear()
    }
}
