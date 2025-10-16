package com.example.di

import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

object DependencyContainer {
    private val dependencyGetters: MutableMap<Identifier, () -> Any> = mutableMapOf()

    fun initialize(vararg module: Module) {
        module.forEach(::initialize)
    }

    fun dependency(identifier: Identifier): Any = dependencyGetters[identifier]?.invoke() ?: error("No dependency defined for $identifier.")

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (!property.hasAnnotation<Dependency>()) return@forEach

            val identifier = Identifier.from(property)
            dependencyGetters[identifier] = {
                property.getter.call(module) ?: error("$property's getter returned null.")
            }
        }
    }
}
