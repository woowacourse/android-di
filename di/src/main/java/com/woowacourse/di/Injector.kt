package com.woowacourse.di

import com.woowacourse.di.annotation.Inject
import com.woowacourse.di.annotation.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object Injector {

    fun <T : Any> createInstance(modelClass: KClass<T>): T {
        val constructor =
            modelClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("Unknown modelClass")

        val params =
            constructor.parameters.associateWith { parameter ->
                val annotation = parameter.findAnnotation<Qualifier>()?.value
                DependencyContainer.instance(parameter.type.jvmErasure, annotation)
            }
        return constructor.callBy(params).also { injectProperty(it) }
    }

    fun <T : Any> injectProperty(instance: T) {
        instance::class.declaredMemberProperties
            .filter { isInjectableProperty(it) }
            .forEach { property ->
                property.isAccessible = true
                property.javaField?.let { field ->
                    val type = field.type.kotlin
                    val qualifier = property.findAnnotation<Qualifier>()?.value
                    val fieldValue = DependencyContainer.instance(type, qualifier)
                    field.set(instance, fieldValue)
                }
            }
    }

    private fun isInjectableProperty(property: KProperty<*>): Boolean {
        return property.hasAnnotation<Inject>() && property is KMutableProperty<*>
    }
}
