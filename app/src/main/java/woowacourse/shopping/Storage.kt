package woowacourse.shopping

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

object Storage {
    val cartRepository: CartRepository = CartRepository()
    val productRepository: ProductRepository = ProductRepository()
}