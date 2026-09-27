package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import kotlin.reflect.KClass

object CartRepositoryModule {
    fun provideCartRepository(): KClass<out CartRepository> = DefaultCartRepository::class
}
