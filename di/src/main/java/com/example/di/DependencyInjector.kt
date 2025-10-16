package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    fun <T : Any> instance(targetClass: KClass<T>): T {
        val instance: T = injectParameters(targetClass)
        injectFields(instance)
        return instance
    }

    private fun <T : Any> injectParameters(targetClass: KClass<T>): T {
        val constructor: KFunction<T> =
            targetClass.primaryConstructor
                ?: error("Unable to find the primary constructor of $targetClass.")
        val parameters: Array<Any> =
            constructor.parameters
                .map(Identifier::from)
                .map(DependencyContainer::dependency)
                .toTypedArray()
        return constructor.call(*parameters)
    }

    private fun injectFields(target: Any) {
        target::class.memberProperties.forEach { property: KProperty1<out Any, *> ->
            property.isAccessible = true
            if (!property.hasAnnotation<Inject>()) return@forEach

            val identifier = Identifier.from(property)
            val mutableProperty =
                property as? KMutableProperty1
                    ?: error("Cannot inject dependency to $property because it is an immutable property.")
            mutableProperty.setter.call(target, DependencyContainer.dependency(identifier))
        }
    }
}
