package com.example.di

import com.example.di.annotation.QualifierType
import kotlin.reflect.KClass

object Container {
    private val instances: MutableMap<Dependency, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
    ): T {
        val dependency = Dependency(type, qualifierType)
        return instances[dependency] as? T ?: Injector.createInstance(type)
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

    fun clear() {
        instances.clear()
    }
}
