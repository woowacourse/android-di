package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

object Singleton {
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
    val cartRepository: CartRepository by lazy { DefaultCartRepository() }
}
