package woowacourse.shopping

import com.android.di_android.ApplicationInjector
import woowacourse.shopping.data.di.ApplicationLifeModule
import woowacourse.shopping.data.di.ApplicationModule

class ShoppingApplication : ApplicationInjector() {
    override fun onCreate() {
        super.onCreate()

        injectModule(ApplicationModule(applicationContext))
        injectModule(ApplicationLifeModule())
    }
}
