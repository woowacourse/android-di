package com.medandro.di.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class InjectField(
    val scope: LifecycleScope = LifecycleScope.APPLICATION,
)
