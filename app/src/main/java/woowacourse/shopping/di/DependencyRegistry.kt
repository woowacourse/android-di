package woowacourse.shopping.di

import kotlin.reflect.KClass

object DependencyRegistry {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> addInstance(
        classType: KClass<*>,
        instance: T,
    ) {
        instances[classType] = instance
    }

    fun <T : Any> getInstanceOrNull(classType: KClass<T>): Any? {
        return instances[classType]
    }
}
