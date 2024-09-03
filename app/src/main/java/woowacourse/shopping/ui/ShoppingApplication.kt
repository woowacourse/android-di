package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.di.DefaultAppModule

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appModule = DefaultAppModule(appContext = this)
    }

    companion object {
        lateinit var appModule: AppModule
            private set
    }
}
