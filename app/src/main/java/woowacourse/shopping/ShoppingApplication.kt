package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.auto.AppContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.init(this)
    }
}
