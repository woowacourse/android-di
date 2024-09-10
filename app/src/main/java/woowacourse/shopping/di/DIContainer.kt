package woowacourse.shopping.di

import woowacourse.shopping.di.module.DIModule
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val singletonInstances = mutableMapOf<KClass<*>, Any>()
    private val interfaceMappings = mutableMapOf<KClass<*>, KClass<*>>()

    fun loadModule(module: DIModule) {
        module.register(this)
    }

    fun <T : Any> registerMapping(
        interfaceClass: KClass<T>,
        implementationClass: KClass<out T>,
    ) {
        interfaceMappings[interfaceClass] = implementationClass
    }

    fun <T : Any> registerInstance(
        interfaceClass: KClass<T>,
        instance: T,
    ) {
        singletonInstances[interfaceClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(kClass: KClass<T>): T {
        singletonInstances[kClass]?.let {
            return it as T
        }

        val actualClass = interfaceMappings[kClass] ?: kClass
        val constructor =
            actualClass.primaryConstructor ?: actualClass.constructors.firstOrNull()
                ?: throw IllegalArgumentException("${actualClass.simpleName}: 생성자를 찾을 수 없습니다")

        val parameters =
            constructor.parameters.map { parameter ->
                val parameterClass =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${parameter.name}: 매개변수 타입이 잘못되었습니다")
                getInstance(parameterClass)
            }

        val instance = constructor.call(*parameters.toTypedArray()) as T
        singletonInstances[kClass] = instance

        return instance
    }
}
