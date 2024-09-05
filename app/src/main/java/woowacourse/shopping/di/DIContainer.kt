package woowacourse.shopping.di

import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.javaType

object DIContainer {
    val instances = mutableMapOf<Class<*>, Any>()

    inline fun <reified T : Any> putInstance(instance: T) {
        instances[T::class.java] = instance
    }

    @OptIn(ExperimentalStdlibApi::class)
    inline fun <reified T : Any> inject(): T {
        val constructor = T::class.primaryConstructor ?: throw Exception()
        val parameters =
            constructor.parameters.map {
                it.type.javaType
            }.map { instances[it] }.toTypedArray()
        return constructor.call(*parameters)
    }
}
