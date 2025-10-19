package com.example.di.scope

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
