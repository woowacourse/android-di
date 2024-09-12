package woowacourse.shopping

import android.app.Application
import com.example.di.DIContainer
import com.example.di.DIContainer.setInstance
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.di.DefaultAppModule
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val appModule = DefaultAppModule(this)
        val cartProductDao = appModule.database.cartProductDao()
        setInstance(CartProductDao::class, cartProductDao)

        setInstance(ProductRepository::class, DefaultProductRepository(), "room")
        setInstance(CartRepository::class, DefaultCartRepository(cartProductDao), "room")
        setInstance(CartRepository::class, InMemoryCartRepository(), "inMemory")

        DIContainer.setViewModelInstance(MainViewModel::class, MainViewModel())
        DIContainer.setViewModelInstance(CartViewModel::class, CartViewModel())
    }
}
