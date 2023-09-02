package woowacourse.shopping.di.container

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository

class RepositoryContainer {
    val productRepository = DefaultProductRepository()
    val cartRepository = DefaultCartRepository()
}
