package com.boogiwoogi.di

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

class DiInjector {

    val instantiator = Instantiator()

    inline fun <reified T : Any> inject(
        module: Module,
        container: InstanceContainer
    ): T {
        val primaryConstructor = requireNotNull(T::class.primaryConstructor)
        val arguments = primaryConstructor.parameters.map { parameter ->
            container.find(parameter)
                ?: instantiator.instantiate(module, parameter)
                    ?.also { instance ->
                        parameter.findAnnotation<Scoped>() ?: container.add(Instance(instance))
                    }
                ?: throw IllegalArgumentException("${parameter::class} 타입의 인스턴스를 생성할 수 없습니다.")
        }

        return primaryConstructor.call(*arguments.toTypedArray())
    }

    inline fun <reified T : Any> inject(
        module: Module,
        container: InstanceContainer,
        target: T
    ) {
        ClazzInfoExtractor.extractInjectMemberProperties(target::class).forEach { memberProperty ->
            val instance = container.find(memberProperty.returnType.jvmErasure)
                ?: instantiator.instantiateProperty(module, memberProperty)
                    ?.also { instance ->
                        memberProperty.findAnnotation<Scoped>() ?: container.add(Instance(instance))
                    }
                ?: throw IllegalArgumentException("${memberProperty::class} 타입의 인스턴스를 생성할 수 없습니다.")

            memberProperty.isAccessible = true
            memberProperty.setter.call(target, instance)
        }
    }
}
