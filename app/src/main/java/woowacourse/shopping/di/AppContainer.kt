package woowacourse.shopping.di

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

interface AppContainer {
    fun dependency(type: KType): Any
}

inline fun <reified T : Any> AppContainer.instance(): T {
    val primaryConstructor: KFunction<T> =
        T::class.primaryConstructor
            ?: error("${T::class.qualifiedName} doesn't have primary constructor")

    val parameters: List<Any> =
        primaryConstructor.parameters.map { parameter: KParameter ->
            dependency(parameter.type)
        }

    return primaryConstructor.call(*parameters.toTypedArray())
}
