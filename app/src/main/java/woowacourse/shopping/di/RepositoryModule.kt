package woowacourse.shopping.di

import woowacourse.shopping.data.entity.CartProductDao
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryModule {

    fun provideCartRepository(cartDao: CartProductDao): CartRepository {
        return DefaultCartRepository(cartDao)
    }

    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
