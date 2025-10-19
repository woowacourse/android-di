package com.example.di.scope

import kotlin.reflect.KClass

object ScopeContainer {
    private val registry = mutableMapOf<KClass<out Annotation>, ScopeHandler>()

    fun setHandler(
        annotation: KClass<out Annotation>,
        handler: ScopeHandler,
    ) {
        registry[annotation] = handler
    }

    fun getHandler(annotation: KClass<out Annotation>): ScopeHandler? = registry[annotation]
}
