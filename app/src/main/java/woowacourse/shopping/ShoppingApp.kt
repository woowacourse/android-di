package woowacourse.shopping

import android.app.Application
import woowa.shopping.di.libs.container.container
import woowa.shopping.di.libs.container.startDI
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startDI {
            container {
                single<ProductRepository> { ProductRepositoryImpl() }
                single<CartRepository> { CartRepositoryImpl() }
            }
        }
    }
}