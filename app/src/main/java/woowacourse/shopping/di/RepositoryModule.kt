package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

object RepositoryModule {
    val cartRepository: CartRepository by lazy { DefaultCartRepository() }
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
}