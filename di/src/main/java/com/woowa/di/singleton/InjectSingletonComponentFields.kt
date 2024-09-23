package com.woowa.di.singleton

import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.jvm.kotlinProperty

fun injectSingletonComponentFields(instance: Any) {
    instance::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter { it.isAnnotationPresent(Inject::class.java) }.map { field ->
        val fieldInstance =
            SingletonComponentManager.getDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        field.set(instance, fieldInstance)
    }
}
