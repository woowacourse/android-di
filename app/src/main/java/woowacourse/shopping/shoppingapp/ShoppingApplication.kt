package woowacourse.shopping.shoppingapp

import com.woowacourse.di.DiApplication
import woowacourse.shopping.data.di.DatabaseModule
import woowacourse.shopping.shoppingapp.di.ApplicationLifecycleModule

class ShoppingApplication : DiApplication() {
    override fun onCreate() {
        super.onCreate()

        injectModule(DatabaseModule(applicationContext))
        injectModule(ApplicationLifecycleModule())
    }
}
