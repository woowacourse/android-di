package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.putInstance(ProductRepository())
        DIContainer.putInstance(CartRepository())
    }
}
