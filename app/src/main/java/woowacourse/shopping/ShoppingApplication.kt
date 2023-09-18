package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.container.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatter

class ShoppingApplication : Application() {
    private lateinit var diContainer: DiContainer
    lateinit var injector: Injector
        private set

    override fun onCreate() {
        super.onCreate()
        setupContainer()
        setupInjector()
    }

    private fun setupContainer() {
        diContainer = DiContainer()
        diContainer.registerProviders {
            provider(CartProductDao::class to ShoppingDatabase.getInstance(applicationContext)::cartProductDao)
            provider(DateFormatter::class to CartActivity::createDateFormatter)
        }
    }

    private fun setupInjector() {
        injector = Injector(diContainer)
    }
}
