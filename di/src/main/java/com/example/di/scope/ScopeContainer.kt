package com.example.di.scope

import kotlin.reflect.KClass

object ScopeContainer {
    val registry = mutableMapOf<KClass<out Annotation>, ScopeHandler>()

    fun setHandler(
        annotation: KClass<out Annotation>,
        handler: ScopeHandler,
    ) {
        registry[annotation] = handler
    }
}
