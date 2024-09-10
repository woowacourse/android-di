package woowacourse.shopping.di

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

class DIContainer(diModule: DIModule) {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    init {
        diModule.injectSingletonInstance()
    }

    private fun DIModule.injectSingletonInstance() =
        singletonInstance().forEach { it.instance.putSingletonInstance(it.type) }

    private fun Any.putSingletonInstance(type: KClass<*>) {
        if (instances.containsKey(type)) {
            throw IllegalArgumentException("이미 해당 클래스의 인스턴스가 존재합니다.")
        }
        instances[type] = this
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
        val instance = classType.constructors.first().call(*arguments.toTypedArray())
        instance.putSingletonInstance(classType)
        instance.injectFieldDependency()
        return instance
    }

    private fun argumentInstance(parameter: KParameter): Any {
        val parameterType = parameter.type.classifier as KClass<*>
        return instances[parameterType] ?: throw IllegalArgumentException("생성자 파라미터 ${parameterType.simpleName}의 인스턴스가 정의되지 않았습니다.")
    }

    private fun Any.injectFieldDependency() {
        val properties = this::class.declaredMemberProperties.filter { it.isLateinit }
        properties.forEach { property ->
            val p = property as KMutableProperty1
            val type = p.returnType.classifier as KClass<*>
            p.isAccessible = true
            val instance = instances[type] ?: throw IllegalArgumentException("필드 ${type.simpleName}의 인스턴스가 정의되지 않았습니다.")
            property.setter.call(this, instance)
        }
    }
}
