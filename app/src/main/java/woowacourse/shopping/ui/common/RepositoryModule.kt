package woowacourse.shopping.ui.common

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

object RepositoryModule {
    val productRepository: ProductRepository = ProductRepositoryImpl()
    val cartRepository: CartRepository = CartRepositoryImpl()
}
