package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DefaultModule
import woowacourse.shopping.di.startDI

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        startDI {
            loadModule(DefaultModule)
        }
    }

    companion object {
        lateinit var instance: ShoppingApplication
    }
}
