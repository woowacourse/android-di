package woowacourse.shopping.data.repository

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object RepositoryModule {
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
    val cartRepository: CartRepository by lazy { DefaultCartRepository() }
}
