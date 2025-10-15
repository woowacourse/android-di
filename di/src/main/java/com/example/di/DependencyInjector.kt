package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object DependencyInjector {
    private val dependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()

    fun initialize(vararg module: Module) {
        module.forEach(::initialize)
    }

    fun <T : Any> instance(targetClass: KClass<T>): T {
        val instance: T = injectParameters(targetClass)
        injectFields(instance)
        return instance
    }

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (property.findAnnotation<Dependency>() == null) return@forEach

            val identifier = Identifier.of(property)
            dependencyGetters[identifier] = {
                property.getter.call(module) ?: error("$property's getter returned null.")
            }
        }
    }

    private fun dependency(identifier: Identifier): Any =
        dependencyGetters[identifier]?.invoke() ?: error("No dependency defined for $identifier.")

    private fun <T : Any> injectParameters(targetClass: KClass<T>): T {
        val constructor: KFunction<T> =
            targetClass.primaryConstructor
                ?: error("Unable to find the primary constructor of $targetClass.")
        val parameters: Array<Any> = constructor.parameters.map(Identifier::of).toTypedArray()
        return constructor.call(*parameters)
    }

    private fun injectFields(target: Any) {
        target::class.memberProperties.forEach { property: KProperty1<out Any, *> ->
            property.isAccessible = true
            if (property.findAnnotation<Inject>() == null) return@forEach

            val identifier = Identifier.of(property)
            val mutableProperty =
                property as? KMutableProperty1
                    ?: error("Cannot inject dependency to $property because it is an immutable property.")
            mutableProperty.setter.call(target, dependency(identifier))
        }
    }
}
