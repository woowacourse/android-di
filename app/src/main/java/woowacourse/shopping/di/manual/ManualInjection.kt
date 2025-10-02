package woowacourse.shopping.di.manual

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

object ManualInjection {
    val cartRepository: CartRepository by lazy { CartRepository() }
    val productRepository: ProductRepository by lazy { ProductRepository() }
}
