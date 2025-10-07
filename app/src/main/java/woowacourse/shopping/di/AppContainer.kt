package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class AppContainer {
    val productRepository: ProductRepository = DefaultProductRepository()
    val cartRepository: CartRepository = DefaultCartRepository()
}
