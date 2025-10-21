package com.example.di

import kotlin.jvm.java

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

    fun inject(
        target: Any,
        scope: ViewModelContainer,
    ) {
        val clazz = target.javaClass
        clazz.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                val qualifier = field.getAnnotation(Qualifier::class.java)?.name
                val dependency = scope.get(field.type.kotlin, qualifier)
                field.isAccessible = true
                field.set(target, dependency)
            }
    }

    fun inject(
        target: Any,
        scope: ActivityContainer,
    ) {
        val clazz = target.javaClass
        clazz.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                val qualifier = field.getAnnotation(Qualifier::class.java)?.name
                val dependency = scope.get(field.type.kotlin, qualifier)
                field.isAccessible = true
                field.set(target, dependency)
            }
    }
}
