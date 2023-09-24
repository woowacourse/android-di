package woowacourse.shopping

import com.di.woogidi.application.DiApplication
import woowacourse.shopping.di.module.ShoppingApplicationModule

class ShoppingApplication : DiApplication() {

    override fun onCreate() {
        super.onCreate()

        injector.applicationModule = ShoppingApplicationModule(this)
    }
}
