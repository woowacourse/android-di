package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    val productRepository by lazy { ProductRepository() }
    val cartRepository by lazy { CartRepository() }
}
