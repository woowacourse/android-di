package woowacourse.shopping

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class AppContainer {
    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }
    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }
}
