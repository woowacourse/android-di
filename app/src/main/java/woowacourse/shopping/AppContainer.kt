package woowacourse.shopping

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl

class AppContainer {
    val cartRepository by lazy { CartRepositoryImpl() }
    val productRepository by lazy { ProductRepositoryImpl() }
}
