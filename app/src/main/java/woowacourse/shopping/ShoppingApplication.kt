package woowacourse.shopping

import woowacourse.di.DIApplication
import woowacourse.di.Injector
import woowacourse.shopping.di.ShoppingApplicationModule

class ShoppingApplication : DIApplication(ShoppingApplicationModule()) {

    override fun onCreate() {
        super.onCreate()

        injector = diInjector
    }

    companion object {
        lateinit var injector: Injector
    }
}
