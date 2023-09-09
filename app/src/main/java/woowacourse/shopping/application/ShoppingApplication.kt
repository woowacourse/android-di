package woowacourse.shopping.application

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.ShoppingContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    lateinit var injector: Injector
        private set

    override fun onCreate() {
        super.onCreate()

        val db: ShoppingDatabase = createRoomDatabase()
        val appContainer: ShoppingContainer = DefaultContainer()
        injector = Injector(appContainer)

        val dao = db.cartProductDao()
        appContainer.createInstance(CartProductDao::class, dao)
        appContainer.createInstance(CartRepository::class, DefaultCartRepository(dao))
        appContainer.createInstance(ProductRepository::class, DefaultProductRepository())
    }

    private fun createRoomDatabase(): ShoppingDatabase {
        return Room.databaseBuilder(
            this,
            ShoppingDatabase::class.java,
            "cart_products",
        ).build()
    }
}
