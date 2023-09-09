package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartDatabase
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.Injector
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        injectRepository()
    }

    private fun injectRepository() {
        val database = Room
            .databaseBuilder(this, CartDatabase::class.java, "kkrong-database")
            .build()
        val cartProductDao = database.cartProductDao()

        Container.addInstance(
            CartProductDao::class,
            cartProductDao,
        )

        Container.addInstance(
            ProductRepository::class,
            Injector.inject(DefaultProductRepository::class),
        )
        Container.addInstance(
            CartRepository::class,
            Injector.inject(DatabaseCartRepository::class),
        )
    }
}
