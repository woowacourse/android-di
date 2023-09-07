package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartDatabase
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.RepositoryContainer
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

        RepositoryContainer.addInstance(
            ProductRepository::class,
            DefaultProductRepository(),
        )
        RepositoryContainer.addInstance(
            CartRepository::class,
            DefaultCartRepository(cartProductDao),
        )
    }
}
