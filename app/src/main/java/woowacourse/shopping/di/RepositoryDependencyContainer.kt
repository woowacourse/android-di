package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class RepositoryDependencyContainer : RepositoryDependency {
    override val cartRepository: CartRepository by lazy { CartRepository() }
    override val productRepository: ProductRepository by lazy { ProductRepository() }
}
