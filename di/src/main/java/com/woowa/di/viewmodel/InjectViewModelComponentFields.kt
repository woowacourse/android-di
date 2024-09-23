package com.woowa.di.viewmodel

import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.jvm.kotlinProperty

fun injectViewModelComponentFields(instance: Any) {
    instance::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter {
        it.isAnnotationPresent(Inject::class.java)
    }.map { field ->
        val fieldInstance =
            ViewModelComponentManager.getDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        field.set(instance, fieldInstance)
    }
}
