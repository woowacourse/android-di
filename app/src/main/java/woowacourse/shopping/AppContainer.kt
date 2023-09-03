package woowacourse.shopping

import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

interface AppContainer {

    val productRepository: ProductRepository
    val cartRepository: CartRepository
}
