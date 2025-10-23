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
    fun <T : Any> instance(
        targetClass: KClass<T>,
        qualifier: Annotation? = null,
    ): T {
        targetClass.primaryConstructor?.let { primaryConstructor: KFunction<T> ->
            val parameters: Array<Any> =
                primaryConstructor.parameters
                    .map { parameter ->
                        val qualifier = Qualifier.from(parameter)
                        instance(parameter.type.classifier as KClass<*>, qualifier)
                    }.toTypedArray()
            return primaryConstructor.call(*parameters)
        }

        val identifier = Identifier(targetClass, qualifier)
        return DependencyContainer.dependency(identifier) as? T
            ?: error("Dependency for $identifier could not be cast to the requested type.")
    }

    fun injectFields(target: Any) {
        target::class.memberProperties.forEach { property: KProperty1<out Any, *> ->
            property.isAccessible = true
            if (!property.hasAnnotation<Inject>()) return@forEach

            val identifier: Identifier = Identifier.from(property)
            val mutableProperty =
                property as? KMutableProperty1
                    ?: error("Cannot inject dependency to field $property because it is immutable.")
            mutableProperty.setter.call(target, DependencyContainer.dependency(identifier))
        }
    }
}
