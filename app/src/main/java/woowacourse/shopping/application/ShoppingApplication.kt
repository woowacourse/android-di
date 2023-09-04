package woowacourse.shopping.application

import android.app.Application
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DefaultAppContainer

class ShoppingApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
