package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.DIContainer

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.setUpInstances(this)
    }
}
