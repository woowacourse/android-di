package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.di.DiContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDI()
    }

    private fun setupDI() {
        val database = DatabaseModule.database(applicationContext)
        DiContainer.addProviders(CartProductDao::class) { DatabaseModule.cartProductDao(database) }

        DiContainer.bind<ProductRepository, DefaultProductRepository>()
        DiContainer.bind<CartRepository, DefaultCartRepository>()
    }
}
