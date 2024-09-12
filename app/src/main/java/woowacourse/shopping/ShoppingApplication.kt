package woowacourse.shopping

import android.app.Application
import com.example.sh1mj1.AppContainer
import com.example.sh1mj1.Qualifier
import com.example.sh1mj1.find
import com.example.sh1mj1.singletonComponent
import woowacourse.shopping.data.CartDao
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository

class ShoppingApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()

        container.add(
            singletonComponent<CartDao>(CartProductDao.instance(this), Qualifier("RoomDao")),
        )

        container.add(
            singletonComponent<ProductRepository>(InMemoryProductRepository(), Qualifier("InMemory")),
            singletonComponent<CartRepository>(InMemoryCartRepository(), Qualifier("InMemory")),
            singletonComponent<CartRepository>(
                DefaultCartRepository(
                    container.find<CartDao>(Qualifier("RoomDao")),
                ),
                Qualifier("RoomDao"),
            ),
        )
    }
}
