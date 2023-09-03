package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryModule {
    fun init() {
        DIContainer.bind(ProductRepository::class, ProductRepositoryImpl())
        DIContainer.bind(CartRepository::class, CartRepositoryImpl())
    }
}