package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class DependencyInjector(
    private val container: DependencyInjectionContainer
) {

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.instantiateParameters()

        return primaryConstructor.call(*arguments)
    }

    fun List<KParameter>.instantiateParameters(): Array<Any?> =
        map { parameter ->
            parameter
                .type
                .jvmErasure
                .instantiateRecursively()
        }.toTypedArray()

    private fun KClass<*>.instantiateRecursively(): Any {
        val constructor = primaryConstructor
            ?: return requireNotNull(container.find(this))

        if (constructor.parameters.isEmpty()) {
            return container.find(this) { constructor.call() }
        }
        val arguments = constructor.parameters.instantiateParameters()

        return constructor.call(*arguments)
    }
}
