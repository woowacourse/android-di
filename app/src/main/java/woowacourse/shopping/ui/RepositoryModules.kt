package woowacourse.shopping.ui

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

object RepositoryModules {
    val productRepository: ProductRepository = ProductRepositoryImpl()
    val cartRepository: CartRepository = CartRepositoryImpl()
}
