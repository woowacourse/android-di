package com.boogiwoogi.di.version2

import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class DiInjector {

    val instantiator = Instantiator()

    inline fun <reified T : Any> inject(
        modules: Modules,
        container: DiContainer
    ): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.map { parameter ->
            container.find(parameter) ?: instantiator.instantiate(modules, parameter).also {
                container.add(Instance(it))
            }
        }
        return primaryConstructor.call(*arguments.toTypedArray())
    }

    inline fun <reified T : Any> inject(
        modules: Modules,
        container: DiContainer,
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
