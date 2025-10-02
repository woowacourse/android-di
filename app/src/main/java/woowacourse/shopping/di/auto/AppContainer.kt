package woowacourse.shopping.di.auto

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

object AppContainer : Container() {
    init {
        bindSingleton(CartRepository::class) { CartRepository() }
        bindSingleton(ProductRepository::class) { ProductRepository() }
    }
}
