package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class DependencyInjector(private val registry: DependencyRegistry) {
    fun <T : Any> createWithAutoDI(classType: KClass<T>): T {
        val constructor = getPrimaryConstructor(classType)
        val dependencies = resolveDependencies(constructor.parameters)
        val instance = constructor.callBy(dependencies)
        registry.addInstance(classType, instance)
        return instance
    }

    private fun <T : Any> getPrimaryConstructor(classType: KClass<T>): KFunction<T> {
        return classType.primaryConstructor ?: throw IllegalArgumentException("주생성자가 존재하지 않습니다.")
    }

    private fun resolveDependencies(parameters: List<KParameter>): Map<KParameter, Any> {
        return parameters.associateWith { parameter ->
            val parameterType = parameter.type.classifier as KClass<*>
            registry.getInstanceOrNull(parameterType)
                ?: throw IllegalArgumentException("해당 파라미터 타입에 해당하는 인스턴스를 찾을 수 없습니다: $parameterType")
        }
    }
}
