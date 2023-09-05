package woowacourse.shopping.di

import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

interface RepositoryDependency {
    val cartRepository: CartRepository
    val productRepository: ProductRepository
}
