package com.m6z1.moongdi

import com.m6z1.moongdi.annotation.InjectField
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

fun KClass<*>.instantiate(context: ScopedDependencyInjector.InjectionContext): Any {
    val constructor =
        primaryConstructor ?: constructors.maxByOrNull { it.parameters.size }
            ?: throw IllegalStateException("생성자를 찾을 수 없음: $simpleName")

    val instance =
        if (constructor.parameters.isEmpty()) {
            constructor.call()
        } else {
            val args =
                constructor.parameters.associateWith { param ->
                    ScopedDependencyContainer.provide(
                        param.type.jvmErasure.java,
                        context.activityId,
                        context.viewModelId,
                    )
                }
            constructor.callBy(args)
        }

    instance::class
        .declaredMemberProperties
        .filter { it.hasAnnotation<InjectField>() }
        .forEach { prop ->
            val field = prop.javaField ?: return@forEach
            field.isAccessible = true
            val value =
                ScopedDependencyContainer.provide(
                    prop.returnType.jvmErasure.java,
                    context.activityId,
                    context.viewModelId,
                )
            field.set(instance, value)
        }
    return instance
}
