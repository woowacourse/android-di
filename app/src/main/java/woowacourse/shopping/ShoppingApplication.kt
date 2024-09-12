package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.InjectedComponent
import woowacourse.shopping.di.Qualifier

class ShoppingApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.add(
            InjectedComponent.InjectedSingletonComponent(ProductRepository::class, InMemoryProductRepository()),
            InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                InMemoryCartRepository(),
                Qualifier("InMemory"),
            ),
            InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                DefaultCartRepository(CartProductDao.instance(this)),
                Qualifier("RoomDao"),
            ),
        )
    }
}
