package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(type: KClass<*>): Any = instances[type] ?: throw NullPointerException("No Instance Found")

    fun setInstance(
        type: KClass<*>,
        instance: Any,
    ) {
        instances[type] = instance
    }

    inline fun <reified T : Any> inject(): T {
        if (instances.containsKey(T::class)) {
            return getInstance(T::class) as T
        }
        val constructor = T::class.primaryConstructor ?: throw IllegalArgumentException("${T::class} has no primary constructor")
        val parameters =
            constructor.parameters
                .map {
                    val clazz = it.type.classifier as KClass<*>
                    getInstance(clazz)
                }.toTypedArray()
        val instance = constructor.call(*parameters)
        setInstance(T::class, instance)
        return instance
    }
}
