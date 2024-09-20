package com.android.di.component

import com.android.di.annotation.DiViewModel
import com.android.di.annotation.Inject
import com.android.di.annotation.hasQualifier
import com.android.di.annotation.qualifierAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DiViewModelComponent {
    private const val ERROR_QUALIFIER_MATCH = "DiViewModelComponent No find Qualifier %s"
    private const val ERROR_INJECT_MATCH = "DiViewModelComponent No find Inject %s"

    fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class

        checkInjectAnnotations(clazz)

        clazz.java.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldAnnotations = field.annotations.toList()

                val fieldInstance =
                    if (fieldAnnotations.hasQualifier()) {
                        val qualifier =
                            fieldAnnotations.qualifierAnnotation()
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

    private fun <T : Any> checkInjectAnnotations(clazz: KClass<T>) {
        clazz.constructors.forEach { constructor ->
            constructor.parameters.forEach { parameter ->
                if (!parameter.hasAnnotation<Inject>()) {
                    throw IllegalArgumentException(ERROR_INJECT_MATCH.format(constructor))
                }
            }
        }
    }
}
