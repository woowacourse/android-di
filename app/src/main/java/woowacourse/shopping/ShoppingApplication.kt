package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.register(ProductRepository::class.java, ProductRepository())
        DIContainer.register(CartRepository::class.java, CartRepository())
    }
}
