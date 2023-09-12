package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.data.repository.PersistentCartRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class DiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val cartDao = ShoppingDatabase.getDatabase(applicationContext).cartProductDao()
        DependencyContainer.addInstance(CartRepository::class, InMemoryCartRepository())
        DependencyContainer.addInstance(CartRepository::class, PersistentCartRepository(cartDao))
        DependencyContainer.addInstance(ProductRepository::class, DefaultProductRepository())
    }
}
