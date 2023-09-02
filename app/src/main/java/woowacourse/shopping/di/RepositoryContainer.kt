package woowacourse.shopping.di

import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.data.ProductDefaultRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class RepositoryContainer {
    val productRepository: ProductRepository by lazy {
        ProductDefaultRepository()
    }

    val cartRepository: CartRepository by lazy {
        CartDefaultRepository()
    }
}
