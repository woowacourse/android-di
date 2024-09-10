package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AutoDIManager.createAutoDIInstance
import woowacourse.shopping.di.AutoDIManager.registerDependency

class ShoppingApplication : Application() {
    private val shoppingDatabase: ShoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        registerDependencies()
    }

    private fun registerDependencies() {
        registerDependency<CartProductDao>(shoppingDatabase.cartProductDao())
        registerDependency<ProductRepository>(ProductRepositoryImpl())
        registerDependency<CartRepository>(createAutoDIInstance<CartRepositoryImpl>())
    }
}
