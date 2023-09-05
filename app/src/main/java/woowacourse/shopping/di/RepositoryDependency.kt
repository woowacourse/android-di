package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

interface RepositoryDependency {
    val cartRepository: CartRepository
    val productRepository: ProductRepository
}
