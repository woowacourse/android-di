package woowacourse.shopping

import com.android.diandroid.ApplicationInjector
import woowacourse.shopping.data.di.ApplicationLifeModule
import woowacourse.shopping.data.di.ApplicationModule

class ShoppingApplication : ApplicationInjector() {
    override fun onCreate() {
        super.onCreate()

        injectModule(ApplicationModule(applicationContext))
        injectModule(ApplicationLifeModule())
    }
}
