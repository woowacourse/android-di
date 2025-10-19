package com.example.di

import android.util.Log
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

class DependencyMapping(
    vararg module: Module,
) {
    private val identifierScopes: MutableMap<Identifier, Scope> = mutableMapOf()
    private val dependencyGetters: Map<Scope, MutableMap<Identifier, () -> Any>> =
        Scope.entries.associateWith { scope: Scope -> mutableMapOf() }

    init {
        module.forEach(::initialize)
    }

    fun get(identifier: Identifier): Any {
        val scope: Scope = scope(identifier)
        val getter: MutableMap<Identifier, () -> Any> = getters(scope)
        return getter[identifier]?.invoke() ?: error("No dependency found for $identifier.")
    }

    fun clear(scope: Scope) {
        Log.wtf("asdf", "Clear: $scope")
        dependencyGetters[scope]?.clear() ?: error("Getters for $scope has not been initialized.")
        dependencyGetters.forEach {
            Log.wtf("asdf", "    ${it.key}")
            it.value.forEach {
                Log.wtf("asdf", "        $it")
            }
            Log.wtf("asdf", " ")
        }
    }

    private fun initialize(module: Module) {
        module::class.memberProperties.forEach { property: KProperty1<out Module, *> ->
            if (!property.hasAnnotation<Dependency>()) return@forEach

            val identifier = Identifier.from(property)
            val scope = Scope.from(property)
            identifierScopes[identifier] = scope
            getters(scope)[identifier] =
                {
                    property.getter.call(module) ?: error("$property's getter returned null.")
                }
        }
    }

    private fun scope(identifier: Identifier): Scope =
        identifierScopes[identifier] ?: error("Scope for $identifier has not been initialized.")

    private fun getters(scope: Scope): MutableMap<Identifier, () -> Any> =
        dependencyGetters[scope] ?: error("Getters for $scope has not been initialized.")
}
