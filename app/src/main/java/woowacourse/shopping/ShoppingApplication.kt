package woowacourse.shopping

import android.app.Application
import com.example.sh1mj1.InjectedComponent.*
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
            InjectedSingletonComponent(
                ProductRepository::class,
                InMemoryProductRepository(),
                Qualifier("InMemory"),
            ),
            InjectedSingletonComponent(
                CartRepository::class,
                InMemoryCartRepository(),
                Qualifier("InMemory"),
            ),
            InjectedSingletonComponent(
                CartRepository::class,
                DefaultCartRepository(CartProductDao.instance(this)),
                Qualifier("RoomDao"),
            ),
        )
    }
}
