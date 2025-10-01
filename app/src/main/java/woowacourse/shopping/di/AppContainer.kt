package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class AppContainer {
    val productRepository: ProductRepository = ProductRepository()
    val cartRepository: CartRepository = CartRepository()
}
