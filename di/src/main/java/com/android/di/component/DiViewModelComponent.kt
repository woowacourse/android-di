package com.android.di.component

import com.android.di.annotation.DiViewModel
import com.android.di.annotation.Inject
import com.android.di.annotation.hasQualifier
import com.android.di.annotation.qualifierAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DiViewModelComponent {
    private const val ERROR_QUALIFIER_MATCH = "DiViewModelComponent No find Qualifier %s"

    fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldAnnotations = field.annotations.toList()
                val fieldInstance = if (fieldAnnotations.hasQualifier()) {
                    val qualifier = fieldAnnotations.qualifierAnnotation()
                        ?: throw IllegalArgumentException(ERROR_QUALIFIER_MATCH.format(field))
                    DiSingletonComponent.matchByQualifier(qualifier)
                } else {
                    DiSingletonComponent.match(field.type.kotlin)
                }
                field.set(instance, fieldInstance)
            }
        }
    }

    fun <T : Any> hasAnnotation(clazz: KClass<T>): Boolean {
        return clazz.hasAnnotation<DiViewModel>()
    }
}
