package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
}

class DefaultAppContainer : AppContainer {
    override val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl()
    }

    override val cartRepository: CartRepository by lazy {
        CartRepositoryImpl()
    }
}