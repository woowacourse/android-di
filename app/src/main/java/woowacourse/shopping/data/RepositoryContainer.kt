package woowacourse.shopping.data

import woowacourse.shopping.application.ShoppingApplication
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryContainer {

    private val db = ShoppingDatabase.getInstance(ShoppingApplication.getApplicationContext())

    val cartRepository: CartRepository = DefaultCartRepository(db)

    val productRepository: ProductRepository = DefaultProductRepository()
}
