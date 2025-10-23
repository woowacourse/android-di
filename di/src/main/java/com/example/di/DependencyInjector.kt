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
        val identifier = Identifier(targetClass, qualifier)
        return runCatching { DependencyContainer.dependency(identifier) }.getOrElse {
            val constructor: KFunction<T> =
                targetClass.primaryConstructor
                    ?: error("Unable to retrieve nor instantiate $targetClass with qualifier $qualifier.")
            val parameters: Array<Any> =
                constructor.parameters
                    .map { parameter ->
                        val qualifier = Qualifier.from(parameter)
                        instance(parameter.type.classifier as KClass<*>, qualifier)
                    }.toTypedArray()
            constructor.call(*parameters)
        } as T
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
