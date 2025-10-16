package com.example.di

import com.sun.tools.javac.code.TypeAnnotationPosition.field
import kotlin.jvm.java

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

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
            val dependency = container.get(field.type.kotlin)
            field.isAccessible = true
            field.set(target, dependency)
        }
    }
}
