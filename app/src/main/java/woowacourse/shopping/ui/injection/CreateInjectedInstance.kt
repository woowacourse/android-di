package woowacourse.shopping.ui.injection

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
            if (parameter.findAnnotation<DIInjection>() != null) {
                val paramType =
                    parameter.type.classifier as? KClass<out Any>
                        ?: error("적절하지 않은 파라미터 타입입니다.")

                val moduleType = moduleTypes.find { paramType.isSubclassOf(it) } as? KClass<out Any>

                val module =
                    ModuleRegistry.getModuleForType(moduleType as KClass)
                        ?: error("${paramType.simpleName} 타입의 모듈이 없습니다.")

                val companionInstance = module.companionObjectInstance

                val kFunc = module.companionObject?.functions?.find { it.name == "getInstance" }
                return@map (kFunc?.call(companionInstance) as Module<*, *>).getDIInstance(paramType as KClass<Nothing>)
            } else {
                error("${parameter.name} @DIInject 어노테이션이 없습니다.")
            }
        }.toTypedArray()

    return primaryConstructor.call(*args)
}
