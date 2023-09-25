package woowacourse.shopping

import woowacourse.shopping.di.module.ApplicationModule
import woowacourse.shopping.ui.DiApplication

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()
        setupApplicationModule()
    }

    private fun setupApplicationModule() {
        registerModule(ApplicationModule::class)
    }
}
