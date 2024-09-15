package woowacourse.shopping

import android.app.Application
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.AutoDIManager.registerDependency
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.util.DependencyProvider

class ShoppingApplication : Application() {
    private val shoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    private val defaultProductRepository by lazy { createAutoDIInstance<DefaultProductRepository>() }

    private val defaultCartRepository by lazy { createAutoDIInstance<DefaultCartRepository>() }

    override fun onCreate() {
        super.onCreate()
        registerDependencies()
        AutoDIManager.provider = DependencyProvider
    }

    private fun registerDependencies() {
        registerDependency<CartProductDao>(shoppingDatabase.cartProductDao())
        registerDependency<ProductRepository>(defaultProductRepository)
        registerDependency<CartRepository>(defaultCartRepository)
    }
}
