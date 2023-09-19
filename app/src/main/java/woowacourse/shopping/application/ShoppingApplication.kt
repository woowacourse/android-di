package woowacourse.shopping.application

import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.DefaultApplicationModule
import woowacourse.shopping.di.Injector

class ShoppingApplication : DIApplication(DefaultApplicationModule::class.java) {
    override fun onCreate() {
        super.onCreate()

        injector = Injector(DefaultApplicationModule(applicationContext))
    }

    companion object {
        lateinit var injector: Injector
    }
}
