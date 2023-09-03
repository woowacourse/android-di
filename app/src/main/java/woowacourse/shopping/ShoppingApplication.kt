package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContainer = object : AppContainer {
            override val productRepository: ProductRepository = ProductRepositoryImpl()
            override val cartRepository: CartRepository = CartRepositoryImpl()
        }
    }

    companion object {
        lateinit var appContainer: AppContainer
    }
}
