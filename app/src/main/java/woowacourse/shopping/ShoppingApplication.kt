package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository

class ShoppingApplication : Application() {
    private val db by lazy { ShoppingDatabase.initialize(applicationContext) }
    private val cartProductDao by lazy { db.cartProductDao() }

    override fun onCreate() {
        super.onCreate()
        initialize(cartProductDao)
    }

    fun initialize(cartProductDao: CartProductDao) {
        DependencyInjector.addInstance(
            ProductRepository::class,
            RepositoryModule.provideProductRepository(),
        )
        DependencyInjector.addInstance(
            CartRepository::class,
            @RoomDB
            RepositoryModule.provideCartRepository(cartProductDao),
            // RoomDB::class
        )
//        DependencyInjector.addInstance(
//            CartRepository::class,
//            @InMemory
//            RepositoryModule.provideCartInMemoryRepository(),
//            InMemory::class
//        )
    }
}
