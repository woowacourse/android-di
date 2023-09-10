package com.boogiwoogi.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class WoogiInjector(
    private val woogiContainer: WoogiContainer = DefaultWoogiContainer()
) {

    inline fun <reified T : Any> inject(): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.instantiateParameters()
        val instance = primaryConstructor.call(*arguments)

        return instance.apply { instantiateMemberProperties() }
    }

    fun List<KParameter>.instantiateParameters(): Array<Any?> =
        map { it.instantiate() }.toTypedArray()

    private fun KParameter.instantiate(): Any = when (hasAnnotation<WoogiProperty>()) {
        true -> instantiateOnQualifier()
            ?: woogiContainer.find(this.type.jvmErasure)
            ?: throw NoSuchElementException()

        false -> type.jvmErasure.instantiateRecursively()
    }

    fun <T> KProperty<T>.instantiate(): Any = when (hasAnnotation<WoogiProperty>()) {
        true -> instantiateOnQualifier()
            ?: woogiContainer.find(this.returnType.jvmErasure)
            ?: throw NoSuchElementException()

        false -> returnType.jvmErasure.instantiateRecursively()
    }

    private fun KAnnotatedElement.instantiateOnQualifier(): Any? {
        if (!annotations.any { it == WoogiQualifier::class }) return null
        val qualifier = annotations.filterIsInstance<WoogiQualifier>().first().name

        return woogiContainer.find(qualifier)
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
                it.setter.call(this, it.instantiate())
            }
    }

    companion object {

        private const val NO_SUCH_CONSTRUCTOR = "주 생성자가 존재하지 않습니다."
    }
}
