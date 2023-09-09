package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class DependencyInjector(
    private val container: Container = DependencyInjectionContainer()
) {

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.instantiateParameters()

        return primaryConstructor.call(*arguments)
    }

    fun List<KParameter>.instantiateParameters(): Array<Any?> =
        map { it.instantiate() }.toTypedArray()

    private fun KParameter.instantiate(): Any = when (hasAnnotation<WoogiProperty>()) {
        true -> container.find(type.jvmErasure) ?: throw NoSuchElementException()
        false -> type.jvmErasure.instantiateRecursively()
    }

    private fun KClass<*>.instantiateRecursively(): Any {
        val constructor = primaryConstructor ?: throw Throwable(NO_SUCH_CONSTRUCTOR)
        if (constructor.parameters.isEmpty()) return constructor.call()

        val arguments = constructor.parameters.instantiateParameters()

        return constructor.call(*arguments)
    }

    companion object {

        private const val NO_SUCH_CONSTRUCTOR = "주 생성자가 존재하지 않습니다."
    }
}
