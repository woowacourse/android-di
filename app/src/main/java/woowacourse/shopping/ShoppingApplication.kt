package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.common.AppContainer
import woowacourse.shopping.common.DefaultAppContainer

class ShoppingApplication : Application() {
    lateinit var defaultAppContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        defaultAppContainer = DefaultAppContainer()
    }
}
