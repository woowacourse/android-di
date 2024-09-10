package woowacourse.shopping.di

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredMemberFunctions

class InstanceContainer(
    modules: List<InjectionModule>,
) {
    private val functionsByReturnType: Map<KType, KFunction<*>> = modules
        .flatMap { module: InjectionModule ->
            module::class.declaredMemberFunctions
        }
        .associateBy(KFunction<*>::returnType)

    private val instances: MutableMap<KType, Any> = mutableMapOf()

    init {
        // 미리 각 모듈의 인스턴스를 캐시에 저장해야 오류가 나지 않음
        saveModuleInstances(modules)
        functionsByReturnType.forEach { (returnType, _) ->
            resolveInstance(returnType)
        }
    }

    fun <T : Any> instanceOf(kType: KType): T {
        val instance = instances[kType]
        checkNotNull(instance) { EXCEPTION_NO_MATCHING_PROPERTY.format(kType) }
        return instance as T
    }

    private fun saveModuleInstances(modules: List<InjectionModule>) {
        modules.forEach { module ->
            instances[module::class.createType()] = module
        }
    }

    private fun resolveInstance(kType: KType): Any {
        instances[kType]?.let { return it }

        // kType에 맞는 함수를 찾음
        val function = functionsByReturnType[kType]
            ?: throw IllegalArgumentException("No function found for return type: $kType")

        // 재귀 주입
        val parameterValues = function.parameters.associateWith { parameter ->
            resolveInstance(parameter.type)
        }
        val instance = function.callBy(parameterValues)
            ?: throw IllegalStateException("Failed to create instance for type: $kType")

        instances[kType] = instance
        return instance
    }

    companion object {
        private const val EXCEPTION_NO_MATCHING_PROPERTY =
            "No matching property found for parameter %s"
    }
}
