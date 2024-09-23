package woowacourse.shopping.di

import android.app.Application
import android.util.Log

abstract class DependencyInjectedApplication : Application() {
    lateinit var applicationDependencyContainer: LifecycleDependencyContainer
        private set

    override fun onCreate() {
        super.onCreate()
        applicationDependencyContainer = ApplicationDependencyContainer()
        applicationDependencyContainer.setApplicationContext(this)
    }
}
