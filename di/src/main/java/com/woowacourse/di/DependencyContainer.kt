package com.woowacourse.di

import kotlin.reflect.KClass

typealias DependencyKey = Pair<KClass<*>, String?>

object DependencyContainer {
    private val instances = mutableMapOf<DependencyKey, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
        qualifier: String? = null,
    ) {
        val key: DependencyKey = classType to qualifier
        if (instances.containsKey(key)) return
        instances[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> instance(
        classType: KClass<T>,
        qualifier: String? = null,
    ): T {
        val key: DependencyKey = classType to qualifier
        return instances[key] as? T ?: Injector.createInstance(classType)
    }

    fun clear() {
        instances.clear()
    }
}
