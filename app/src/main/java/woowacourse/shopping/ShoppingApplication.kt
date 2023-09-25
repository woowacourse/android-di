package woowacourse.shopping

import woowacourse.di.DIApplication
import woowacourse.di.Injector
import woowacourse.shopping.di.AppModule

class ShoppingApplication : DIApplication(AppModule()) {

    override fun onCreate() {
        super.onCreate()

        injector = diInjector
    }

    companion object {
        lateinit var injector: Injector
    }
}
