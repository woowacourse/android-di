package woowacourse.shopping.application

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.ShoppingContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val db: ShoppingDatabase = createRoomDatabase()
        val dao = db.cartProductDao()
        val appContainer: ShoppingContainer = DefaultContainer()
        injector = Injector(appContainer)

        appContainer.createInstance(CartProductDao::class, dao)
        appContainer.createInstance(
            ProductRepository::class,
            injector.create(DefaultProductRepository::class)
        )
        appContainer.createInstance(
            Qualifier.DATABASE,
            injector.create(DefaultCartRepository::class)
        )
        appContainer.createInstance(
            Qualifier.IN_MEMORY,
            injector.create(InMemoryCartRepository::class)
        )
    }

    private fun createRoomDatabase(): ShoppingDatabase {
        return Room.databaseBuilder(
            this,
            ShoppingDatabase::class.java,
            "cart_products",
        ).build()
    }

    companion object {
        lateinit var injector: Injector
    }
}
