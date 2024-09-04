package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

interface AppModule {
    val productRepository: ProductRepository

    val cartRepository: CartRepository
}
