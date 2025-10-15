package woowacourse.shopping

import android.app.Application
import com.example.di.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartProductDao
import woowacourse.shopping.data.annotation.LocalDatabaseCartProductDao
import woowacourse.shopping.data.annotation.InMemoryCartProductDao as InMemoryCartProductDaoQualifier
import woowacourse.shopping.di.DatabaseModule
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDI()
    }

    private fun setupDI() {
        val database = DatabaseModule.database(applicationContext)
        DiContainer.addProviders(
            CartProductDao::class,
            LocalDatabaseCartProductDao::class,
        ) { DatabaseModule.cartProductDao(database) }

        DiContainer.addProviders(
            CartProductDao::class,
            InMemoryCartProductDaoQualifier::class,
        ) { InMemoryCartProductDao() }

        DiContainer.bind(ProductRepository::class, DefaultProductRepository::class)
        DiContainer.bind(CartRepository::class, DefaultCartRepository::class)
    }
}
