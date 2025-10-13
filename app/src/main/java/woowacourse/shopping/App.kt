package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.di.AppContainer

class App : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer
        appContainer.init(this)
    }
}
