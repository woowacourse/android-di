package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class DefaultAppContainer : AppContainer {
    override val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }

    override val cartRepository: CartRepository by lazy {
        DefaultCartRepository()
    }
}
