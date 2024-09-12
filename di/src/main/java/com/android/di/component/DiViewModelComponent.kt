package com.android.di.component

import com.android.di.annotation.DiViewModel
import com.android.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object DiViewModelComponent {
    fun <T : Any> injectFields(instance: T) {
        val clazz = instance::class.java
        clazz.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                val fieldInstance = DiSingletonComponent.match(field.type.kotlin)
                field.set(instance, fieldInstance)
            }
        }
    }

    fun <T : Any> hasAnnotation(clazz: KClass<T>): Boolean {
        return clazz.hasAnnotation<DiViewModel>()
    }
}
