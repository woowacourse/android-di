package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository

object RepositoryContainer {

    val productRepository: ProductRepository = ProductRepositoryImpl()
    val cartRepository: CartRepository = CartRepositoryImpl()
}
