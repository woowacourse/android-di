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
        appContainer.registerProviders {
            provider(CartProductDao::class to ShoppingDatabase.getInstance(applicationContext)::cartProductDao)
        }
        appContainer.registerQualifiers {
            qualifier(Qualifier("defaultProductRepository") to DefaultProductRepository::class)
            qualifier(Qualifier("databaseCartRepository") to DatabaseCartRepository::class)
            qualifier(Qualifier("inMemoryCartRepository") to InMemoryCartRepository::class)
        }
    }
}
