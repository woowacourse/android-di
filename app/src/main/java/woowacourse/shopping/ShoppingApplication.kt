package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class ShoppingApplication : Application() {
    lateinit var repositoryModule: RepositoryModule
        private set

    override fun onCreate() {
        super.onCreate()
        repositoryModule =
            RepositoryModule(
                CartRepository::class to CartRepositoryImpl(),
                ProductRepository::class to ProductRepositoryImpl(),
            )
    }
}
