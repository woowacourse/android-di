package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.di.AutoDIManager

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerDependencies()
    }

    private fun registerDependencies() {
        AutoDIManager.registerDependency(ProductRepository::class, ProductRepositoryImpl())
        AutoDIManager.registerDependency(CartRepository::class, CartRepositoryImpl())
    }
}
