package woowacourse.shopping

import android.app.Application
import android.content.Context
import woowacourse.shopping.container.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.createDateFormatter

class ShoppingApplication : Application() {
    private lateinit var diContainer: DiContainer
    lateinit var injector: Injector
        private set

    override fun onCreate() {
        super.onCreate()
        setupContainer()
        setupInjector()

        injector.addDependency("ApplicationContainer", Context::class, this)
    }

    private fun setupContainer() {
        diContainer = DiContainer()
        diContainer.registerProviders {
            provider(ShoppingDatabase::class to ShoppingDatabase::getInstance)
            provider(CartProductDao::class to ShoppingDatabase::cartProductDao)
            provider(DateFormatter::class to ::createDateFormatter)
        }
    }

    private fun setupInjector() {
        injector = Injector(diContainer)
    }
}
