package woowacourse.shopping.core.di

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

interface DependencyContainer {
    fun dependency(
        type: KType,
        annotations: List<Annotation> = emptyList(),
    ): Any
}

inline fun <reified T : Any> DependencyContainer.createInstance(): T {
    val primaryConstructor: KFunction<T> =
        T::class.primaryConstructor
            ?: error("${T::class.qualifiedName} doesn't have primary constructor")

    val parameters: List<Any> =
        primaryConstructor.parameters.map { parameter: KParameter ->
            dependency(parameter.type, parameter.annotations)
        }

    return primaryConstructor.call(*parameters.toTypedArray())
}
