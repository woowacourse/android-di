package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultAppContainer : AppContainer() {
    override val cartRepository: CartRepository by lazy {
        DefaultCartRepository()
    }
    override val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }
}
