package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DiContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> setInstance(
        kClass: KClass<T>,
        instance: T,
    ) {
        instances[kClass] = instance
    }

    fun <T : Any> getInstance(kClass: KClass<T>): T =
        instances[kClass] as? T ?: run {
            val primaryConstructor =
                requireNotNull(kClass.primaryConstructor) { kClass.java.simpleName }

            val newInstance = primaryConstructor.call()
            instances[kClass] = newInstance
            newInstance
        }
}
