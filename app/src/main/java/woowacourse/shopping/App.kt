package woowacourse.shopping

import android.app.Application

class App : Application() {
    lateinit var container: DependencyContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DependencyContainer()
    }
}
