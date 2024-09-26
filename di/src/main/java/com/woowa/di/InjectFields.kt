package com.woowa.di

import com.woowa.di.component.ComponentManager
import javax.inject.Inject
import kotlin.reflect.jvm.kotlinProperty

inline fun <reified T : ComponentManager> injectFieldFromComponent(instance: Any) {
    instance::class.java.declaredFields.onEach { field ->
        field.isAccessible = true
    }.filter { it.isAnnotationPresent(Inject::class.java) }.map { field ->
        val fieldType = field.type.kotlin
        val qualifier = field.kotlinProperty?.findQualifierClassOrNull()
        val fieldInstance = T::class.objectInstance?.getDIInstance(fieldType, qualifier) ?: error("${T::class.simpleName}를 object로 선언해주세요")
        field.set(instance, fieldInstance)
    }
}