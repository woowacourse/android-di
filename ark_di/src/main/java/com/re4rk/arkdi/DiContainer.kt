package com.re4rk.arkdi

import com.re4rk.arkdi.util.qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

open class DiContainer(private val parentDiContainer: DiContainer? = null) {

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        val injectedConstructor = clazz.constructors.filter { it.hasAnnotation<ArkInject>() }

        val constructor = when (injectedConstructor.size) {
            0 -> getPrimaryConstructor(clazz)
            1 -> injectedConstructor.first()
            else -> throw IllegalArgumentException("DiInject annotation must be on only one constructor")
        }

        val args = constructor.parameters.associateWith { parameter -> get(parameter) }
        return constructor.callBy(args)
    }

    private fun <T : Any> getPrimaryConstructor(clazz: KClass<T>): KFunction<T> {
        val primaryConstructor = clazz.primaryConstructor
            ?: throw IllegalArgumentException("Primary constructor not found")

        if (primaryConstructor.parameters.all { it.isOptional }) {
            return primaryConstructor
        }

        throw IllegalArgumentException("Primary constructor must be all optional")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: KClass<T>): T? {
        val method = getMethod(clazz) ?: return parentDiContainer?.get(clazz)
        val parameters = method.parameters.associateWith { parameter -> getInstance(parameter) }
        return method.callBy(parameters) as T
    }

    private fun <T : Any> getMethod(clazz: KClass<T>): KFunction<*>? {
        return this::class.declaredFunctions.filter { it.qualifier == clazz.qualifier }
            .firstOrNull { function ->
                function.isAccessible = true
                function.javaMethod?.returnType?.simpleName == clazz.simpleName
            }
    }

    fun get(kParameter: KParameter): Any? {
        val method = getParameter(kParameter) ?: return parentDiContainer?.get(kParameter)
        val parameters = method.parameters.associateWith { parameter -> getInstance(parameter) }
        return method.callBy(parameters)
    }

    private fun getParameter(kParameter: KParameter): KFunction<*>? {
        return this::class.declaredFunctions.filter { it.qualifier == kParameter.qualifier }
            .firstOrNull { property ->
                property.isAccessible = true
                property.returnType == kParameter.type
            }
    }

    private fun getInstance(parameter: KParameter): Any? {
        return when {
            parameter.type.jvmErasure.isSubclassOf(DiContainer::class) -> this@DiContainer
            else -> get(parameter)
        }
    }
}
