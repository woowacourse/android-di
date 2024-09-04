package woowacourse.shopping.di

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class DIContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun put(
        classType: KClass<*>,
        instance: Any,
    ) {
        if (instances.containsKey(classType)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[classType] = instance
    }

    fun <T : Any> get(classType: KClass<T>): T {
        val instance = instances[classType] ?: create(classType)
        return instance as T
    }

    private fun <T : Any> create(classType: KClass<T>): T {
        val parameters = classType.constructors.first().parameters
        val arguments = parameters.map(::argumentInstance)
        return classType.constructors.first().call(*arguments.toTypedArray()).also {
            put(classType, it)
        }
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        val argument = instances[parameterType] ?: throw IllegalArgumentException("생성자 파라미터의 인스턴스가 정의되지 않았습니다.")
        return argument
    }
}
