package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

class AppContainer {
    val productRepository = DefaultProductRepository()
    val cartRepository = DefaultCartRepository()
}
