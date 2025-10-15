package woowacourse.shopping.di

import kotlin.reflect.KClass

interface Container {
    fun <T : Any> register(
        kClass: KClass<T>,
        qualifier: String? = null,
        provider: () -> T,
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
