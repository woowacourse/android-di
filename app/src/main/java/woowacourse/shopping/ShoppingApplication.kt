package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.AppContainer

class ShoppingApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
