package com.example.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

fun <T : Any> inject(
    modelClass: KClass<T>,
    dependencies: DependencyProvider,
): T {
    val constructor =
        requireNotNull(modelClass.primaryConstructor) { "No suitable constructor found for $modelClass" }
    val parameters =
        constructor.parameters
            .filter { it.hasAnnotation<Inject>() }
            .associateWith { kParameter ->
                val paramsTypes = kParameter.type.jvmErasure
                if (kParameter.hasAnnotation<Qualifier>()) {
                    val type = requireNotNull(kParameter.findAnnotation<Qualifier>()?.type)
                    dependencies.getInstance(paramsTypes, type) ?: inject(paramsTypes, dependencies)
                } else {
                    dependencies.getInstance(paramsTypes) ?: inject(paramsTypes, dependencies)
                }
            }
    return constructor.callBy(parameters).also { injectFields(it, dependencies) }
}

private fun <T : Any> injectFields(
    instance: T,
    dependencies: DependencyProvider,
) {
    val properties =
        instance::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .filter { it.hasAnnotation<Inject>() }

    properties.forEach { property ->
        property.isAccessible = true
        property.setter.call(instance, dependencies.getInstance(property.returnType.jvmErasure))
    }
}
