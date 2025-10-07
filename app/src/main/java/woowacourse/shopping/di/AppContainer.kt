package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl

class AppContainer {
    val productRepository = ProductRepositoryImpl()
    val cartRepository = CartRepositoryImpl()
}
