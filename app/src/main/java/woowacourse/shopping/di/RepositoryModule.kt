package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object RepositoryModule {
    private val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    private val cartRepository: CartRepository by lazy { CartRepositoryImpl() }

    fun provideProductRepository(): ProductRepository = productRepository

    fun provideCartRepository(): CartRepository = cartRepository
}
