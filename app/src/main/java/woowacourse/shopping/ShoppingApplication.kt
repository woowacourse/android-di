package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.container.DependencyContainer
import woowacourse.shopping.di.inject.CustomInjector
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    lateinit var injector: CustomInjector
        private set

    override fun onCreate() {
        super.onCreate()
        injector = CustomInjector()

        DependencyContainer.setInstance(
            CartProductDao::class,
            ShoppingDatabase.getDatabase(this).cartProductDao(),
        )
        DependencyContainer.setInstance(
            ProductRepository::class,
            injector.inject(InMemoryProductRepository::class),
        )
        DependencyContainer.setInstance(
            CartRepository::class,
            injector.inject(DatabaseCartRepository::class),
        )
        DependencyContainer.setInstance(
            CartRepository::class,
            injector.inject(InMemoryCartRepository::class),
        )
    }
}
