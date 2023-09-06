package woowacourse.shopping.data.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class RepositoryModule : Module {
    val productRepository: ProductRepository = DefaultProductRepository()
    val cartRepository: CartRepository = DefaultCartRepository()
}