package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppModule
import woowacourse.shopping.di.DIContainer

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DIContainer.loadModule(AppModule())
    }
}
