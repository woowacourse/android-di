package woowacourse.shopping.di

import kotlin.reflect.KClass

interface DependencyRepository {
    fun <T : Any> get(kClass: KClass<T>): T?

    fun <T : Any> save(kClass: KClass<T>, instance: T)
}
