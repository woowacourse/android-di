package woowacourse.shopping

import com.now.androdi.application.ApplicationInjectable
import woowacourse.shopping.di.module.ApplicationModule

class ShoppingApplication : ApplicationInjectable() {
    override fun onCreate() {
        super.onCreate()
        injectModule(ApplicationModule(applicationContext))
    }
}
