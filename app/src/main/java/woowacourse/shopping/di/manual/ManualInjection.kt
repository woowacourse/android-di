package woowacourse.shopping.di.manual

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl

object ManualInjection {
    val cartRepository: CartRepositoryImpl by lazy { CartRepositoryImpl() }
    val productRepository: ProductRepositoryImpl by lazy { ProductRepositoryImpl() }
}
