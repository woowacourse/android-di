package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

object ManualInjection {
    val cartRepository: CartRepository by lazy { CartRepository() }
    val productRepository: ProductRepository by lazy { ProductRepository() }
}
