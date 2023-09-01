package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl

class RepositoryContainer {

    val productRepository: ProductRepositoryImpl by lazy {
        ProductRepositoryImpl()
    }

    val cartRepository: CartRepositoryImpl by lazy {
        CartRepositoryImpl()
    }
}
