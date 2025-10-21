package com.example.di

import java.rmi.server.LogStream
import kotlin.jvm.java

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val name: String)

object FieldInjector {
    fun inject(
        target: Any,
        container: ShoppingContainer,
    ) {
        val clazz = target.javaClass
        clazz.declaredFields
            .filter { field ->
                field.isAnnotationPresent(Inject::class.java)
            }.forEach { field ->
                val qualifier = field.getAnnotation(Qualifier::class.java)?.name
                val dependency = container.get(field.type.kotlin, qualifier)
                field.isAccessible = true
                field.set(target, dependency)
            }
    }
}
