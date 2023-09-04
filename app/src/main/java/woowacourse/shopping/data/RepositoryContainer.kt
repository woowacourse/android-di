package woowacourse.shopping.data

import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryContainer {

    private val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }

    private val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }
}
