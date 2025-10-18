package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.core.di.DependencyContainer.instance
import woowacourse.shopping.core.di.DependencyContainer.register
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.PersistentCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        register(CartProductDao::class) { CartProductDao(this) }
        register(ProductRepository::class) { DefaultProductRepository() }
        register(CartRepository::class, "persistent") {
            PersistentCartRepository(
                instance(
                    CartProductDao::class,
                ),
            )
        }
        register(CartRepository::class, "inMemory") { InMemoryCartRepository() }
    }
}
