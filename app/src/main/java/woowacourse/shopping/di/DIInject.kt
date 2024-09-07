package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

inline fun <reified T : Any> inject(): T {
    val primaryConstructor: KFunction<T> =
        T::class.primaryConstructor
            ?: throw NullPointerException("No Primary Constructor Found")

    val parameters = primaryConstructor.parameters

    val instances =
        parameters.map {
            val type: KClass<*> = it.type.jvmErasure
            DIContainer.getInstance(type)
        }

    return primaryConstructor.call(*instances.toTypedArray())
}
