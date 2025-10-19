package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField


object Injector {

    fun inject(targetInstance: Any, ownerComponent: Component) {
        val targetKClass = targetInstance::class

        for (property in targetKClass.declaredMemberProperties) {
            val javaField = property.javaField ?: continue

            val hasInjectAnnotation = javaField.isAnnotationPresent(Inject::class.java)
            if (!hasInjectAnnotation) continue

            javaField.isAccessible = true

            val dependencyKClass = property.returnType.classifier as? KClass<*>
                ?: throw IllegalStateException()

            val qualifierAnnotation = javaField.annotations.firstOrNull { annotation ->
                annotation.annotationClass.annotations.any { metaAnnotation -> metaAnnotation is Qualifier }
            }?.annotationClass

            val dependencyInstance =
                DiContainer.get(dependencyKClass, ownerComponent, qualifierAnnotation)
            javaField.set(targetInstance, dependencyInstance)
        }
    }
}

