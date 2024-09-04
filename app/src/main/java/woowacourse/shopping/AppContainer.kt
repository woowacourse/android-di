package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

abstract class AppContainer {
    abstract val cartRepository: CartRepository

    abstract val productRepository: ProductRepository
}
