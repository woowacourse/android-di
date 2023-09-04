package woowacourse.shopping.data

import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingContainer {

    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }

    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }
}
