package woowacourse.shopping

import android.app.Application
import com.example.sh1mj1.Qualifier
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    lateinit var container: com.example.sh1mj1.AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.add(
            com.example.sh1mj1.InjectedComponent.InjectedSingletonComponent(ProductRepository::class, InMemoryProductRepository()),
            com.example.sh1mj1.InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                InMemoryCartRepository(),
                Qualifier("InMemory"),
            ),
            com.example.sh1mj1.InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                DefaultCartRepository(CartProductDao.instance(this)),
                Qualifier("RoomDao"),
            ),
        )
    }
}
