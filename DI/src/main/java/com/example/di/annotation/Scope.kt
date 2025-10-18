package com.example.di.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scope(
    val value: ScopeType,
)

enum class ScopeType {
    SINGLETON,
    ACTIVITY_SCOPED,
    VIEWMODEL_SCOPED,
}
