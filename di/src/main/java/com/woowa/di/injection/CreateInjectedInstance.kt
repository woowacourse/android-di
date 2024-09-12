package com.woowa.di.injection

import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

fun <T : Any> createInjectedInstance(clazz: KClass<out T>): T {
    val primaryConstructor =
        clazz.primaryConstructor
            ?: error("${clazz.simpleName}는 주 생성자가 없습니다.")

    val moduleTypes = ModuleRegistry.moduleTypes().toList()
    val args =
        primaryConstructor.parameters.map { parameter ->
            parameter.findAnnotation<DIInjection>()
                ?: error("${parameter.name} @DIInject 어노테이션이 없습니다.")
            val paramType =
                parameter.type.classifier as? KClass<out Any>
                    ?: error("적절하지 않은 파라미터 타입입니다.")

            val module = getModule(moduleTypes, paramType)

            val qualifier =
                parameter.annotations.firstOrNull { annotation ->
                    annotation.annotationClass.findAnnotation<Qualifier>() != null
                }?.annotationClass

            return@map if (qualifier != null) {
                module.getDIInstance(
                    paramType as KClass<Nothing>,
                    qualifier,
                )
            } else {
                module.getDIInstance(paramType as KClass<Nothing>)
            }
        }.toTypedArray()

    return primaryConstructor.call(*args)
}

private fun getModule(
    moduleTypes: List<KClass<*>>,
    moduleKey: KClass<out Any>,
): Module<*, *> {
    val moduleType = moduleTypes.find { moduleKey.isSubclassOf(it) } as? KClass<out Any>

    val moduleClazz =
        ModuleRegistry.getModuleForType(moduleType)
            ?: error("${moduleKey.simpleName} 타입의 모듈이 없습니다.")

    val companionInstance = moduleClazz.companionObjectInstance

    val kFunc = moduleClazz.companionObject?.functions?.find { it.name == "getInstance" }

    val module = kFunc?.call(companionInstance) as Module<*, *>
    return module
}
