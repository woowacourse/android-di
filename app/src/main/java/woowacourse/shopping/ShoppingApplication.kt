package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppDI

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDI.initialize(this)
    }
}
