package woowacourse.shopping.di

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class DIContainer(diModule: DIModule) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        diModule.injectSingletonInstance()
    }

    private fun DIModule.injectSingletonInstance() = singletonInstance().forEach { it.putSingletonInstance() }

    private fun Module.putSingletonInstance() {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[type] = instance
    }

    fun <T : Any> instance(classType: KClass<T>): T {
        return create(classType)
    }

    fun <T : Any> singletonInstance(classType: KClass<T>): T {
        val instance = instances[classType] ?: create(classType)
        return instance as T
    }

    private fun <T : Any> create(classType: KClass<T>): T {
        val parameters = classType.constructors.first().parameters
        val arguments = parameters.map(::argumentInstance)
        return classType.constructors.first().call(*arguments.toTypedArray()).also {
            Module(classType, it).putSingletonInstance()
        }
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        return instances[parameterType] ?: throw IllegalArgumentException("생성자 파라미터의 인스턴스가 정의되지 않았습니다.")
    }
}
