package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class ShoppingApplication : Application() {
    private lateinit var appContainer: AppContainer
    lateinit var injector: Injector
        private set

    override fun onCreate() {
        super.onCreate()
        setupContainer()
        setupInjector()
    }

    private fun setupContainer() {
        appContainer = AppContainer()
        appContainer.registerProviders {
            provider(CartProductDao::class to ShoppingDatabase.getInstance(applicationContext)::cartProductDao)
        }
    }

    private fun setupInjector() {
        injector = Injector(appContainer)
    }
}
