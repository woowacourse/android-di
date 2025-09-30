package woowacourse.shopping.data.di

import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class RepositoryModule {
    val productRepository = ProductRepository()

    val cartRepository = CartRepository()
}
