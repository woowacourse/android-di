package woowacourse.shopping

import android.app.Application
import android.content.Context
import com.example.alsonglibrary2.di.AutoDIManager
import com.example.alsonglibrary2.di.AutoDIManager.createAutoDIInstance
import com.example.alsonglibrary2.di.AutoDIManager.registerDependency
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.util.QualifierDependencyProvider

class ShoppingApplication : Application() {
    private val shoppingDatabase by lazy { ShoppingDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        registerDependencies()
        AutoDIManager.qualifierDependencyProvider = QualifierDependencyProvider
        shoppingAppContext = this
    }

    private fun registerDependencies() {
        registerDependency<CartProductDao>(shoppingDatabase.cartProductDao())
        registerDependency<CartRepository>(defaultCartRepository)
    }

    companion object {
        lateinit var shoppingAppContext: Context
        val defaultCartRepository by lazy { createAutoDIInstance<DefaultCartRepository>() }
        val inMemoryCartRepository by lazy { createAutoDIInstance<InMemoryCartRepository>() }
        val dateFormatter: DateFormatter by lazy { DateFormatter(shoppingAppContext) }
    }
}
