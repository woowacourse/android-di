package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    val instances = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T : Any> putInstance(instance: T) {
        instances[T::class] = instance
    }

    inline fun <reified T : Any> inject(): T {
        if (instances.containsKey(T::class)) {
            return instances[T::class] as T
        }
        val constructor = T::class.primaryConstructor ?: throw IllegalArgumentException("${T::class} has no primary constructor")
        val parameters =
            constructor.parameters.map {
                val clazz = it.type.classifier as KClass<*>
                instances[clazz] ?: throw IllegalArgumentException("$clazz is not initialized")
            }.toTypedArray()
        val instance = constructor.call(*parameters)
        putInstance(instance)
        return instance
    }
}
