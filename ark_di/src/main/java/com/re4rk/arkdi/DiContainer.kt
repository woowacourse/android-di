package com.re4rk.arkdi

import com.re4rk.arkdi.util.qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

open class DiContainer(private val parentDiContainer: DiContainer? = null) {

    fun <T : Any> createInstance(clazz: KClass<T>): T {
        val injectedConstructor = clazz.constructors.filter { it.hasAnnotation<ArkInject>() }

        val constructor = when (injectedConstructor.size) {
            0 -> getPrimaryConstructor(clazz)
            1 -> injectedConstructor.first()
            else -> throw IllegalArgumentException("DiInject annotation must be on only one constructor")
        }

        val args = constructor.parameters.associateWith { parameter -> getInstance(parameter) }
        return constructor.callBy(args).apply {
            this@DiContainer.inject(this)
        }
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
    fun <T : Any> getInstance(clazz: KClass<T>): T? {
        return getInstance(clazz.starProjectedType, clazz.qualifier) as T?
    }

    private fun getInstance(kParameter: KParameter): Any? {
        return getInstance(kParameter.type, kParameter.qualifier)
    }

    private fun getInstance(kType: KType, qualifier: String?): Any? {
        val method = getKFunction(kType, qualifier)
            ?: return parentDiContainer?.getInstance(kType, qualifier)
        val parameters = method.parameters.associateWith { parameter -> getArgument(parameter) }
        return method.callBy(parameters)
    }

    private fun getKFunction(kType: KType, qualifier: String?): KFunction<*>? {
        return this::class.declaredFunctions.filter { it.qualifier == qualifier }
            .firstOrNull { kFunction ->
                kFunction.isAccessible = true
                kFunction.returnType == kType
            }
    }

    private fun getArgument(parameter: KParameter): Any? {
        return when {
            parameter.type.jvmErasure.isSubclassOf(DiContainer::class) -> this@DiContainer
            else -> getInstance(parameter)
        }
    }

    fun inject(instance: Any) {
        instance::class.declaredMemberProperties.filter { it.hasAnnotation<ArkInject>() }
            .forEach { property ->
                property.isAccessible = true
                property.javaField?.set(
                    instance,
                    getInstance(property.returnType.jvmErasure),
                )
            }
    }
}
