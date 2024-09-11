package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import woowacourse.shopping.di.module.DIModule
import kotlin.reflect.KClass

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
    fun <T : Any> resolve(type: KClass<T>): T {
        singletonInstances[type]?.let { return it as T }

        val implementationType = interfaceMappings[type] ?: type

        val constructor =
            implementationType.constructors.firstOrNull {
                it.annotations.any { annotation -> annotation is Inject }
            } ?: implementationType.constructors.first()

        val parameters =
            constructor.parameters.map { parameter ->
                val parameterType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException("${parameter.name}: 매개변수 타입이 잘못되었습니다")
                resolve(parameterType)
            }

        val instance = constructor.call(*parameters.toTypedArray()) as T
        registerInstance(type, instance)

        return instance
    }
}
