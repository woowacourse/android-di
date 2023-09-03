package woowacourse.shopping.di

import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class RepositoryContainer : Container {
    val productRepository: ProductRepository = DefaultProductRepository()
    val cartRepository: CartRepository = DefaultCartRepository()
}
