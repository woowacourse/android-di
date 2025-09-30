package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

object AppContainer {
    val cartRepository: CartRepository by lazy { CartRepositoryImpl() }
    val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
}
