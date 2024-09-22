package com.woowa.di.viewmodel

import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.jvm.kotlinProperty

inline fun <reified T : Any?> injectViewModelComponentFields(instance: T) {
    T::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter {
        it.isAnnotationPresent(Inject::class.java)
    }.map { field ->
        val fieldInstance =
            ViewModelComponentManager2.getDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        field.set(instance, fieldInstance)
    }
}
