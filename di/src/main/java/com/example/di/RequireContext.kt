package com.example.di

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class RequireContext(
    val contextType: ContextType,
) {
    enum class ContextType {
        ACTIVITY,
        APPLICATION,
    }
}
