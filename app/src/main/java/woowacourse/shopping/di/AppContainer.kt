package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> addProviders(kClazz: KClass<T>, instance: T) {
        providers[kClazz] = instance
    }

    fun <T : Any> getProvider(kClazz: KClass<T>) = providers[kClazz] ?: createInstance(kClazz)

    private fun <T : Any> createInstance(kClazz: KClass<T>): T {
        val constructor = kClazz.primaryConstructor ?: throw IllegalArgumentException()
        val arguments = constructor.parameters.associateWith {
            getProvider(it.type.classifier as KClass<*>)
        }
        return constructor.callBy(arguments)
    }
}