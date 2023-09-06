package woowacourse.shopping.di.container

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryContainer {
    private val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }
    private val cartRepository: CartRepository by lazy {
        DefaultCartRepository()
    }
}
