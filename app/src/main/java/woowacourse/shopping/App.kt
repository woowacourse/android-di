package woowacourse.shopping

import android.app.Application
import woowacourse.peto.di.DependencyInjector

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val container = AppContainer(this)
        DependencyInjector.init(container.dependencyContainer)
    }
}
