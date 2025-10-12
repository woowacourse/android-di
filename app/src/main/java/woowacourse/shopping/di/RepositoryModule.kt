package woowacourse.shopping.di

import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

object RepositoryModule {
    val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    val cartRepository: CartRepository by lazy { InMemoryCartRepository() }
}
