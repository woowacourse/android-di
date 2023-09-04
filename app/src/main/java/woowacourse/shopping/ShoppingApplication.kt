package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.module
import woowacourse.shopping.di.startDI
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val dataModule = module {
            provide<ProductRepository> { ProductRepositoryImpl() }
            provide<CartRepository> { CartRepositoryImpl() }
        }
        startDI {
            loadModule(dataModule)
        }
    }
}
