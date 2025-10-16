package com.yrsel.di

import java.util.concurrent.ConcurrentHashMap

private const val ERROR_NOT_FOUND_DEFINITION_KEY = "등록되지 않은 의존성입니다. Definition : %s"

object DependencyContainer {
    private val definitions: ConcurrentHashMap<DefinitionKey, Provider<Any>> = ConcurrentHashMap()

    fun init(vararg modules: Module) {
        definitions.clear()
        modules.forEach(::register)
    }

    private fun register(module: Module) {
        val resolved: Map<DefinitionKey, Provider<Any>> = ModuleResolver.resolve(module)
        resolved.forEach { (key, value) -> definitions[key] = value }
    }

    internal fun get(key: DefinitionKey): Provider<Any> =
        definitions[key] ?: error(
            ERROR_NOT_FOUND_DEFINITION_KEY.format(key.toString()),
        )
}
