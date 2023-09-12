package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.annotation.Qualifier
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
        appContainer.providers {
            provider(CartProductDao::class to ShoppingDatabase.getInstance(applicationContext)::cartProductDao)
        }
        appContainer.addQualifier(
            Qualifier("defaultProductRepository"),
            DefaultProductRepository::class,
        )
        appContainer.addQualifier(
            Qualifier("databaseCartRepository"),
            DatabaseCartRepository::class,
        )
        appContainer.addQualifier(
            Qualifier("inMemoryCartRepository"),
            InMemoryCartRepository::class,
        )
    }
}
