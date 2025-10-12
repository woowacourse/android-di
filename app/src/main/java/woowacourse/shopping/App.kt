package woowacourse.shopping

import android.app.Application
import woowacourse.peto.di.DependencyInjector

class App : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        DependencyInjector.init(container.dependencyContainer)
    }
}
