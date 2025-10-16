package com.example.di

import kotlin.jvm.java

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

object FieldInjector {
    fun inject(
        target: Any,
        container: ShoppingContainer,
    ) {
        val clazz = target.javaClass
        for (field in clazz.declaredFields) {
            if (field.isAnnotationPresent(Inject::class.java)) {
                val dependency = container.get(field.type.kotlin)
                field.isAccessible = true
                field.set(target, dependency)
            }
        }
    }
}
