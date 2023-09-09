package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.common.AppContainer
import woowacourse.shopping.common.DefaultAppContainer

class ShoppingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        defaultAppContainer = DefaultAppContainer.create()
    }

    companion object {
        lateinit var defaultAppContainer: AppContainer
    }
}
