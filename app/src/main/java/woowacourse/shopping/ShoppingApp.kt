package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.DiContainer

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DiContainer.setInstance(CartRepository::class, CartRepository())
        DiContainer.setInstance(ProductRepository::class, ProductRepository())
    }
}
