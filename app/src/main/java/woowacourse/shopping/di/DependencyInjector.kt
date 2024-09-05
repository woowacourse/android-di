package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyInjector(private val registry: DependencyRegistry = DependencyRegistry) {
    fun <T : Any> createAndInject(classType: KClass<T>): T {
        val constructor =
            classType.primaryConstructor ?: throw IllegalArgumentException("주생성자가 존재하지 않습니다.")
        val constructorArgs = mutableListOf<Any>()
        constructor.parameters.forEach { parameter ->
            val parameterType = parameter.type.classifier as KClass<*>
            val instance =
                registry.getInstanceOrNull(parameterType)
                    ?: throw IllegalArgumentException("해당 타입의 인스턴스를 찾을 수 없습니다.")
            constructorArgs.add(instance)
        }
        val instance = constructor.call(*constructorArgs.toTypedArray())
        registry.addInstance(classType, instance)
        return instance
    }
}
