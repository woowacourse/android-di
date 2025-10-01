package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl

class ShoppingApplication : Application() {
    val productRepository by lazy { ProductRepositoryImpl() }
    val cartRepository by lazy { CartRepositoryImpl() }
}
