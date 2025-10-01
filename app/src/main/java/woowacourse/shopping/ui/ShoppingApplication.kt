package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.di.AppContainer

class ShoppingApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}