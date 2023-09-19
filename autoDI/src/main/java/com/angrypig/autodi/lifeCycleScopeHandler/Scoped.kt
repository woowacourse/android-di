package com.angrypig.autodi.lifeCycleScopeHandler

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScopedProperty(val delegated: Boolean = true)

@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scoped()




