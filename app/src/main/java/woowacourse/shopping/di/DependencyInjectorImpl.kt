package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyInjectorImpl : DependencyInjector {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val creating = mutableSetOf<KClass<*>>()

    override fun <T : Any> setInstance(
        kClass: KClass<T>,
        instance: T,
    ) {
        instances[kClass] = instance
    }

    override fun <T : Any> getInstance(kClass: KClass<T>): T {
        return instances[kClass] as? T ?: run {
            createInstance(kClass)
            return instances[kClass] as T
        }
    }

    override fun createInstance(kClass: KClass<*>) {
        if (creating.contains(kClass)) throw IllegalStateException("순환 참조")
        creating.add(kClass)

        val primaryConstructor =
            requireNotNull(kClass.primaryConstructor) { kClass.java.simpleName }

        val params =
            primaryConstructor.parameters.associateWith { param ->
                val paramClass = requireNotNull(param.type.classifier as? KClass<*>)
                getInstance(paramClass)
            }
        val instance = primaryConstructor.callBy(params)
        instances[kClass] = instance
        creating.remove(kClass)
    }
}
