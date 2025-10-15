package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacouse.shopping.di.Container
import woowacouse.shopping.di.annotation.Qualifier

class ShoppingApplication : Application() {
    private val database: ShoppingDatabase by lazy {
        Room
            .databaseBuilder(
                this,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
    }

    private val cartProductDao by lazy { database.cartProductDao() }

    private val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }

    private val roomCartRepository: CartRepository by lazy { CartRepositoryImpl(cartProductDao) }

    private val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }

    val container = Container()

    override fun onCreate() {
        super.onCreate()
        container.registerInstances(ProductRepository::class, productRepository)
        container.registerInstances(CartRepository::class, roomCartRepository, "room")
        container.registerInstances(CartRepository::class, inMemoryCartRepository, "inMemory")
    }
}
