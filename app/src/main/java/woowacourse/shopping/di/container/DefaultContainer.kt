package woowacourse.shopping.di.container

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DefaultContainer : ShoppingContainer {
    override val productRepository: ProductRepository = DefaultProductRepository()
    override val cartRepository: CartRepository = DefaultCartRepository()
}
