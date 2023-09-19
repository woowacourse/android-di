package woowacourse.shopping

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import woowacourse.shopping.container.DiContainer
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.lifecycleobserver.ApplicationLifecycleObserver
import woowacourse.shopping.lifecycleobserver.DefaultApplicationLifecycleObserver
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.createDateFormatter

class ShoppingApplication :
    Application(),
    ApplicationLifecycleObserver by DefaultApplicationLifecycleObserver() {
    private lateinit var diContainer: DiContainer
    lateinit var injector: Injector
        private set

    init {
        setupContainer()
        setupInjector()
        setupLifecycleObserver()
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

    private fun setupLifecycleObserver() {
        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        setupLifecycleObserver(lifecycle, this, injector)
    }
}
