package com.angrypig.autodi.lifeCycleScopeHandler

import com.angrypig.autodi.lifeCycleScopeHandler.annotation.activity.ActivityLifeCycleScope

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScopedProperty(val delegated: Boolean = true)

@Target(AnnotationTarget.CONSTRUCTOR)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scoped()




