package woowacourse.shopping

import android.app.Application
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.AutoDIManager.registerDependency
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.util.DependencyProvider

class ShoppingApplication : Application() {
    private val shoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    private val productRepositoryImpl by lazy { createAutoDIInstance<ProductRepositoryImpl>() }

    private val cartRepositoryImpl by lazy { createAutoDIInstance<CartRepositoryImpl>() }

    override fun onCreate() {
        super.onCreate()
        registerDependencies()
        AutoDIManager.provider = DependencyProvider
    }

    private fun registerDependencies() {
        registerDependency<CartProductDao>(shoppingDatabase.cartProductDao())
        registerDependency<ProductRepository>(productRepositoryImpl)
        registerDependency<CartRepository>(cartRepositoryImpl)
    }
}
