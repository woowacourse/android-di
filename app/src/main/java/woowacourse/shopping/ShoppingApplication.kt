package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
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
        val database = Room
            .databaseBuilder(this, ShoppingDatabase::class.java, "shopping_database")
            .build()
        val cartProductDao = database.cartProductDao()

        DependencyContainer.setInstance(CartProductDao::class, cartProductDao)
        DependencyContainer.setInstance(
            ProductRepository::class,
            injector.inject(DefaultProductRepository::class),
        )
        DependencyContainer.setInstance(
            CartRepository::class,
            injector.inject(DefaultCartRepository::class),
        )
    }
}
