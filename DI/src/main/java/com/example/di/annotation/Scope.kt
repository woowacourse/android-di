package com.example.di.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scope(
    val value: KClass<out ScopeType>,
)

sealed class ScopeType {
    object Singleton : ScopeType()

    object ActivityScoped : ScopeType()

    object ViewModelScoped : ScopeType()
}
