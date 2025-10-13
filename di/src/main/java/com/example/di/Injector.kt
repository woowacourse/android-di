package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

object Injector {
    fun inject(target: Any) {
        val kClass = target::class
        for (property in kClass.declaredMemberProperties) {
            property.javaField?.let { field ->
                if (field.isAnnotationPresent(Inject::class.java)) {
                    field.isAccessible = true

                    val propertyType = property.returnType.classifier as KClass<*>
                    val instance = DiContainer.getProvider(propertyType)

                    field.set(target, instance)
                }
            }
        }
    }
}
