package woowacourse.shopping.di.container

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DefaultContainer(db: ShoppingDatabase) : ShoppingContainer {
    private val dao = db.cartProductDao()

    override val productRepository: ProductRepository = DefaultProductRepository()
    override val cartRepository: CartRepository = DefaultCartRepository(dao)
}

