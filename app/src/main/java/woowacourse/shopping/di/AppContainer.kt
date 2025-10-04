package woowacourse.shopping.di

import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class AppContainer {
    val cartRepository: CartRepository by lazy {
        DefaultCartRepository()
    }

    val productRepository: ProductRepository by lazy {
        DefaultProductRepository()
    }
}
