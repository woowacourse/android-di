package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

class AppContainer {
    val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    val cartRepository: CartRepository by lazy { CartRepositoryImpl() }
}
