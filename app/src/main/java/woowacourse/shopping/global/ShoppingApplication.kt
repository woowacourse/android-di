package woowacourse.shopping.global

import android.app.Application
import woowacourse.shopping.di.module.ApplicationModule
import woowacourse.shopping.di.module.DefaultApplicationModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        applicationModule = DefaultApplicationModule()
    }

    companion object {
        lateinit var applicationModule: ApplicationModule
            private set
    }
}
