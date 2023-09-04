package woowacourse.shopping.di.container

import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.LocalProductRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

object RepositoryContainer {
    private val productRepository: ProductRepository by lazy {
        LocalProductRepository()
    }
    private val cartRepository: CartRepository by lazy {
        LocalCartRepository()
    }
}
