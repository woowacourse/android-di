package woowacourse.shopping.data

import woowacourse.shopping.application.ShoppingApplication
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryContainer {

    private val dao = ShoppingDatabase.getInstance(ShoppingApplication.getApplicationContext()).cartProductDao()

    val cartRepository: CartRepository = DefaultCartRepository(dao)

    val productRepository: ProductRepository = DefaultProductRepository()
}
