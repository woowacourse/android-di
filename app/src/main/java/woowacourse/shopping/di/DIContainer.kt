package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    val instances = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T : Any> putInstance(instance: T) {
        instances[T::class] = instance
    }

    inline fun <reified T : Any> inject(): T {
        val constructor = T::class.primaryConstructor ?: throw Exception()
        val parameters =
            constructor.parameters.map {
                val clazz = it.type.classifier as KClass<*>
                instances[clazz]
            }.toTypedArray()
        return constructor.call(*parameters)
    }
}
