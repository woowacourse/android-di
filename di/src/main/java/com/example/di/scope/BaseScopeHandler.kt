package com.example.di.scope

import kotlin.reflect.KClass

abstract class BaseScopeHandler : ScopeHandler {
    abstract val scopeAnnotation: KClass<out Annotation>
}
