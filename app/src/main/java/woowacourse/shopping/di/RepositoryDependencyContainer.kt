package woowacourse.shopping.di

import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class RepositoryDependencyContainer : RepositoryDependency {
    override val cartRepository: CartRepository by lazy { DefaultCartRepository() }
    override val productRepository: ProductRepository by lazy { DefaultProductRepository() }
}
