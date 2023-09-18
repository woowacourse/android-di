package com.boogiwoogi.di

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class DiInjector {

    val instantiator = Instantiator()

    inline fun <reified T : Any> inject(
        modules: Modules,
        container: InstanceContainer
    ): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.map { parameter ->
            container.find(parameter) ?: instantiator.instantiate(modules, parameter).also {
                parameter.findAnnotation<Scoped>() ?: container.add(Instance(it))
            }
        }
        return primaryConstructor.call(*arguments.toTypedArray())
    }

    inline fun <reified T : Any> inject(
        modules: Modules,
        container: InstanceContainer,
        target: T
    ) {
        ClazzInfoExtractor.extractInjectMemberProperties(target::class).forEach { memberProperty ->
            val instance = container.find(memberProperty.returnType.jvmErasure)
                ?: instantiator.instantiateProperty(modules, memberProperty).also {
                    container.add(Instance(it))
                }

            memberProperty.isAccessible = true
            memberProperty.setter.call(target, instance)
        }
    }
}
