package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import kotlin.reflect.KClass

object RepositoryModule {
    fun defaultCartRepository(): KClass<DefaultCartRepository> = DefaultCartRepository::class

    fun defaultProductRepository(): KClass<DefaultProductRepository> =
        DefaultProductRepository::class
}