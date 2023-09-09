package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class DependencyInjector(
    private val container: Container = DependencyInjectionContainer()
) {

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.instantiateParameters()
        val instance = primaryConstructor.call(*arguments)

        return instance.apply { instantiateMemberProperties() }
    }

    fun List<KParameter>.instantiateParameters(): Array<Any?> =
        map { it.instantiate() }.toTypedArray()

    fun KParameter.instantiate(): Any = when (hasAnnotation<WoogiProperty>()) {
        true -> container.find(type.jvmErasure) ?: throw NoSuchElementException()
        false -> type.jvmErasure.instantiateRecursively()
    }

    fun <T> KProperty<T>.instantiate(): Any = when (hasAnnotation<WoogiProperty>()) {
        true -> container.find(returnType.jvmErasure) ?: throw NoSuchElementException()
        false -> returnType.jvmErasure.instantiateRecursively()
    }

    private fun KClass<*>.instantiateRecursively(): Any {
        val constructor = primaryConstructor ?: throw Throwable(NO_SUCH_CONSTRUCTOR)
        if (constructor.parameters.isEmpty()) return constructor.call()

        val arguments = constructor.parameters.instantiateParameters()

        return constructor.call(*arguments)
    }

    inline fun <reified T : Any> T.instantiateMemberProperties() {
        this::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
            .forEach {
                it.isAccessible = true
                it.setter.call(
                    this,
                    it.instantiate()
                )
            }
    }

    companion object {

        private const val NO_SUCH_CONSTRUCTOR = "주 생성자가 존재하지 않습니다."
    }
}
