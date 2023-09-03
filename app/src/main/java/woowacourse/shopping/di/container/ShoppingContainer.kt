package woowacourse.shopping.di.container

import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

interface ShoppingContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
}
