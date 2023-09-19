package woowacourse.shopping.application

import woowacourse.shopping.di.DIApplication
import woowacourse.shopping.di.DefaultApplicationModule

class ShoppingApplication : DIApplication(DefaultApplicationModule::class.java) {
    override fun onCreate() {
        super.onCreate()
    }
}
