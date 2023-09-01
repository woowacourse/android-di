package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.di.injector.modules
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        modules {
            inject<ProductRepository>(DefaultProductRepository())
            inject<CartRepository>(DefaultCartRepository())
        }
    }
}
