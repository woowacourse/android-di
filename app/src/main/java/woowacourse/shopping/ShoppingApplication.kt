package woowacourse.shopping

import android.app.Application
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.AutoDIManager.registerDependency
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.util.QualifierDependencyProvider

class ShoppingApplication : Application() {
    private val shoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        registerDependencies()
        AutoDIManager.qualifierDependencyProvider = QualifierDependencyProvider
    }

    private fun registerDependencies() {
        registerDependency<ProductRepository>(defaultProductRepository)
        registerDependency<CartProductDao>(shoppingDatabase.cartProductDao())
        registerDependency<CartRepository>(defaultCartRepository)
        registerDependency<DateFormatter>(DateFormatter(this))
    }

    companion object {
        val defaultProductRepository by lazy { createAutoDIInstance<DefaultProductRepository>() }
        val defaultCartRepository by lazy { createAutoDIInstance<DefaultCartRepository>() }
        val inMemoryCartRepository by lazy { createAutoDIInstance<InMemoryCartRepository>() }
    }
}
