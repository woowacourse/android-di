package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

interface AppModule {
    val productRepository: ProductRepository

    val cartRepository: CartRepository
}
