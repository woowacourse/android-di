package com.example.di

import com.example.di.annotation.QualifierType
import kotlin.reflect.KClass

object DIContainer {
    private val instances: MutableMap<Pair<KClass<*>, QualifierType?>, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(
        type: KClass<T>,
        qualifierType: QualifierType?,
    ): T {
        return instances[Pair(type, qualifierType)] as? T ?: DIInjector.createInstance(type)
    }

    fun <T : Any> addInstance(
        type: KClass<T>,
        qualifierType: QualifierType?,
        instance: Any,
    ) {
        if (!instances.containsKey(Pair(type, qualifierType))) {
            instances[Pair(type, qualifierType)] = instance
        }
    }

    fun clear() {
        instances.clear()
    }
}
