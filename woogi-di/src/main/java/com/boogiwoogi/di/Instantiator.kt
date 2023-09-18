package com.boogiwoogi.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class Instantiator {

    private fun instantiateParameters(modules: Modules, parameters: List<KParameter>): Array<Any?> =
        parameters.map { instantiate(modules, it) }.toTypedArray()

    fun instantiate(modules: Modules, parameter: KParameter): Any = when {
        parameter.hasAnnotation<Qualifier>() -> parameter.findAnnotation<Qualifier>()?.run {
            modules.provideInstanceOf(simpleName)
        } ?: throw NoSuchElementException()

        parameter.hasAnnotation<Inject>() -> modules.provideInstanceOf(parameter.type.jvmErasure)
            ?: throw NoSuchElementException()

        else -> parameter.type.jvmErasure.instantiateRecursively(modules)
    }

    private fun <T> KProperty<T>.instantiate(modules: Modules): Any = when {
        hasAnnotation<Qualifier>() -> findAnnotation<Qualifier>()?.run {
            modules.provideInstanceOf(simpleName)
        } ?: throw NoSuchElementException()

        hasAnnotation<Inject>() -> modules.provideInstanceOf(this.returnType.jvmErasure)
            ?: returnType.jvmErasure.instantiateRecursively(modules)

        else -> {}
    }

    private fun KClass<*>.instantiateRecursively(modules: Modules): Any {
        val constructor = primaryConstructor ?: throw Throwable(NO_SUCH_CONSTRUCTOR)
        if (constructor.parameters.isEmpty()) return constructor.call()

        val arguments = instantiateParameters(modules, constructor.parameters)

        return constructor.call(*arguments)
    }

    fun instantiateProperty(modules: Modules, property: KMutableProperty<*>): Any {
        return property.instantiate(modules)
    }

    companion object {

        private const val NO_SUCH_CONSTRUCTOR = "생성자 없음"
    }
}
