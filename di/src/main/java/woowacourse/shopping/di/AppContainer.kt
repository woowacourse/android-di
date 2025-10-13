package woowacourse.shopping.di

import kotlin.reflect.KClass

interface AppContainer {
    fun <T : Any> register(
        kClass: KClass<T>,
        instance: T,
        qualifier: String? = null,
    )

    fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String? = null,
    ): T

    fun <T : Any> canResolve(
        kClass: KClass<T>,
        qualifier: String? = null,
    ): Boolean
}
