package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication: Application() {
    val cartRepository: CartRepository by lazy { DefaultCartRepository() }
    val productRepository: ProductRepository by lazy { DefaultProductRepository() }
}
