package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class AppContainer {
    val productRepository:ProductRepository by lazy {
        ProductRepository()
    }
    val cartRepository: CartRepository by lazy {
        CartRepository()
    }
}



