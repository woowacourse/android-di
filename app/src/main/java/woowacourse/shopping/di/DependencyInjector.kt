package woowacourse.shopping.di

import kotlin.reflect.KClass

interface DependencyInjector {
    fun <T : Any> setInstance(
        kClass: KClass<T>,
        instance: T,
    )

    fun <T : Any> getInstance(kClass: KClass<T>): T

    fun createInstance(kClass: KClass<*>)
}
