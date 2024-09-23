package com.woowa.di.activity

import com.woowa.di.findQualifierClassOrNull
import javax.inject.Inject
import kotlin.reflect.jvm.kotlinProperty

fun injectActivityComponentFields(instance: Any) {
    instance::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter { it.isAnnotationPresent(Inject::class.java) }.map { field ->
        val fieldInstance =
            ActivityComponentManager.getDIInstance(
                field.type.kotlin,
                field.kotlinProperty?.findQualifierClassOrNull(),
            )
        field.set(instance, fieldInstance)
    }
}
