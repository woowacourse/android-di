package woowacourse.shopping.di.container

import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.LocalProductRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class RepositoryContainer {
    val postRepository: ProductRepository by lazy {
        LocalProductRepository()
    }
    val cartRepository: CartRepository by lazy {
        LocalCartRepository()
    }
}
