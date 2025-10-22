package com.example.di

import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation

class DependencyMapping(
    vararg module: Module,
) {
    private val identifierScopes: MutableMap<Identifier, Scope> = mutableMapOf()
    private val dependencyGetters: Map<Scope, MutableMap<Identifier, () -> Any>> =
        Scope.entries.associateWith { mutableMapOf() }

    private val dependencyMapping: Map<Scope, MutableMap<Identifier, Any>> =
        Scope.entries.associateWith { mutableMapOf() }

    init {
        initialize(*module)
    }

    fun get(identifier: Identifier): Any {
        val scope: Scope = scope(identifier)
        val mapping: MutableMap<Identifier, Any> = mapping(scope)

        return mapping[identifier] ?: run {
            val getter: MutableMap<Identifier, () -> Any> = getters(scope)
            val instance =
                getter[identifier]?.invoke() ?: error("No dependency found for $identifier.")
            mapping[identifier] = instance
            return instance
        }
    }

    fun clear(scope: Scope) {
        mapping(scope).clear()
    }

    private fun initialize(vararg module: Module) {
        val temp = mutableMapOf<Identifier, () -> Any>()

        module.forEach { module: Module ->
            module::class.functions.forEach { function: KFunction<*> ->
                if (!function.hasAnnotation<Dependency>()) return@forEach

                val identifier = Identifier.from(function)
                temp[identifier] = { function.call(module) ?: error("") }
            }
        }

        module.forEach { module: Module ->
            module::class.functions.forEach { function: KFunction<*> ->
                if (!function.hasAnnotation<Dependency>()) return@forEach

                val identifier = Identifier.from(function)
                val scope = Scope.from(function)
                identifierScopes[identifier] = scope

                getters(scope)[identifier] =
                    {
                        val arguments =
                            function.parameters
                                .filter { parameter: KParameter ->
                                    parameter.kind == KParameter.Kind.VALUE
                                }.map { parameter: KParameter ->
                                    temp[Identifier.from(parameter)]?.invoke()
                                        ?: error("${Identifier.from(parameter)} not defined in any module")
                                }.toTypedArray()
                        function.call(module, *arguments)
                            ?: error("${function.returnType}'s getter returned null.")
                    }
            }
        }
    }

    private fun scope(identifier: Identifier): Scope =
        identifierScopes[identifier] ?: error("Scope for $identifier has not been initialized.")

    private fun getters(scope: Scope): MutableMap<Identifier, () -> Any> =
        dependencyGetters[scope] ?: error("Getters for $scope has not been initialized.")

    private fun mapping(scope: Scope): MutableMap<Identifier, Any> =
        dependencyMapping[scope] ?: error("Dependency for $scope has not been initialized.")
}
