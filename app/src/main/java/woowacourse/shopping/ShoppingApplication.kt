package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import com.bandal.di.AppContainer
import com.bandal.di.BandalInjectorAppContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.data.repository.InMemoryProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        defaultAppContainer = BandalInjectorAppContainer

        addDefaultInstance(defaultAppContainer)
    }

    private fun addDefaultInstance(defaultAppContainer: AppContainer) {
        defaultAppContainer.addInstance(CartProductDao::class, initCartProductDao())
        defaultAppContainer.addInstance(
            CartRepository::class,
            DatabaseCartRepository::class,
        )
        defaultAppContainer.addInstance(CartRepository::class, InMemoryCartRepository::class)
        defaultAppContainer.addInstance(ProductRepository::class, InMemoryProductRepository::class)
    }

    private fun initCartProductDao(): CartProductDao {
        val database = Room
            .databaseBuilder(this, ShoppingDatabase::class.java, "shopping_database")
            .build()
        return database.cartProductDao()
    }

    companion object {
        lateinit var defaultAppContainer: AppContainer
    }
}
