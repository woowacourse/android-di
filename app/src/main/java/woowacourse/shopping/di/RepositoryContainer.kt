package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

class RepositoryContainer : Container {

    val productRepository: ProductRepository = DefaultProductRepository()
    val cartRepository: CartRepository = DefaultCartRepository(DaoContainer.cartProductDao)
}
