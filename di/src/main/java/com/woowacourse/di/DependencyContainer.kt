package com.woowacourse.di

import com.woowacourse.di.annotation.QualifierType
import kotlin.reflect.KClass

typealias DependencyKey = Pair<KClass<*>, QualifierType?>

object DependencyContainer {
    private val instances = mutableMapOf<DependencyKey, Any>()

    fun <T : Any> addInstance(
        classType: KClass<T>,
        instance: Any,
        qualifier: QualifierType? = null,
    ) {
        val key: DependencyKey = classType to qualifier
        if (instances.containsKey(key)) return
        instances[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> instance(
        classType: KClass<T>,
        qualifier: QualifierType? = null,
    ): T {
        val key: DependencyKey = classType to qualifier
        return instances[key] as? T ?: Injector.createInstance(classType)
    }

    fun <T : Any> removeInstance(
        classType: KClass<T>,
        qualifier: QualifierType? = null,
    ) {
        val key: DependencyKey = classType to qualifier
        instances.remove(key)
    }

    fun clear() {
        instances.clear()
    }

    fun size(): Int {
        return instances.size
    }
}
