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
        return if (containsInstance(
                type,
                qualifierType,
            )
        ) {
            instances[dependency] as T
        } else {
            DIInjector.createInstance(type)
        }
    }

    fun <T : Any> addInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
        instance: Any,
    ) {
        val dependency = Dependency(type, qualifierType)
        if (!containsInstance(type, qualifierType)) {
            instances[dependency] = instance
        }
    }

    fun <T : Any> removeInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
    ) {
        val dependency = Dependency(type, qualifierType)
        if (containsInstance(type, qualifierType)) {
            instances.remove(dependency)
        }
    }

    fun <T : Any> containsInstance(
        type: KClass<T>,
        qualifierType: QualifierType? = null,
    ): Boolean {
        val dependency = Dependency(type, qualifierType)
        return instances.containsKey(dependency)
    }

    fun clear() {
        instances.clear()
    }
}
