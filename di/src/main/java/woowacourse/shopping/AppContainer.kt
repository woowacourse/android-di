package woowacourse.shopping

import woowacourse.shopping.annotation.SingleInstance
import woowacourse.shopping.dslbuilder.ProviderBuilder
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation

class AppContainer {
    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
    private var providers: Map<KClass<*>, KFunction<*>> = emptyMap()

    fun registerProviders(block: ProviderBuilder.() -> Unit) {
        providers = ProviderBuilder().apply(block).build()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getSavedInstanceOf(implementationClass: KClass<out T>): T? {
        return instances[implementationClass] as? T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getProviderOf(implementationClass: KClass<out T>): KFunction<T>? {
        return providers[implementationClass] as? KFunction<T>
    }

    fun <T : Any> saveInstance(implementationClass: KClass<out T>, instance: T) {
        if (implementationClass.hasAnnotation<SingleInstance>()) {
            instances[implementationClass] = instance
        }
    }
}
