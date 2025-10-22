package com.yrsel.di

import android.app.Application
import android.content.Context
import java.util.concurrent.ConcurrentHashMap

private const val ERROR_NOT_FOUND_DEFINITION_KEY = "등록되지 않은 의존성입니다. Definition : %s"

object DependencyContainer {
    private val definitions: ConcurrentHashMap<DefinitionKey, ScopedProvider<Any>> =
        ConcurrentHashMap()

    fun init(
        application: Application,
        vararg modules: Module,
    ) {
        ContextProvider.init(application.applicationContext)
        definitions.clear()
        modules.forEach(::register)

        application.registerActivityLifecycleCallbacks(DIActivityLifecycleCallbacks)
    }

    private fun register(module: Module) {
        val resolved: Map<DefinitionKey, ScopedProvider<Any>> = ModuleResolver.resolve(module)
        resolved.forEach { (key, value) -> definitions[key] = value }
    }

    internal fun get(key: DefinitionKey): ScopedProvider<Any> = definitions[key] ?: error(ERROR_NOT_FOUND_DEFINITION_KEY.format(key))

    fun activityContext(): Context = ContextProvider.getContext()
}
