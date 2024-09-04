package woowacourse.shopping.data

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
}
