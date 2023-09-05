package woowacourse.shopping.data

import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

object RepositoryContainer {

    val cartRepository: CartRepository = CartRepositoryImpl()

    val productRepository: ProductRepository = ProductRepositoryImpl()
}
