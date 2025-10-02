package woowacourse.shopping.di.auto

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

object AppContainer : Container() {
    init {
        bindSingleton(CartRepository::class) { CartRepositoryImpl() }
        bindSingleton(ProductRepository::class) { ProductRepositoryImpl() }
    }
}
