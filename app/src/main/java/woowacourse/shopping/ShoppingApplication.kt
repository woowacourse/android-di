package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter
import woowacouse.shopping.di.Container
import woowacouse.shopping.di.DiFactory

class ShoppingApplication : Application() {
    private val database: ShoppingDatabase by lazy {
        Room
            .databaseBuilder(
                this,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()
    }

    private val container by lazy { Container() }
    val diFactory by lazy { DiFactory(container, this) }

    private val cartProductDao by lazy { database.cartProductDao() }

    private val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }

    private val roomCartRepository: CartRepository by lazy { CartRepositoryImpl(cartProductDao) }

    private val inMemoryCartRepository: CartRepository by lazy { InMemoryCartRepository() }
    private val dateFormatter: DateFormatter by lazy { DateFormatter(this) }

    override fun onCreate() {
        super.onCreate()
        container.registerInstances(ProductRepository::class, productRepository)
        container.registerInstances(CartRepository::class, roomCartRepository, "room")
        container.registerInstances(CartRepository::class, inMemoryCartRepository, "inMemory")
        container.registerInstances(DateFormatter::class, dateFormatter)
    }
}
