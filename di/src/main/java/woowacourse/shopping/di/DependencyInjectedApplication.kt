package woowacourse.shopping.di

import android.app.Application
import woowacourse.shopping.di.container.ApplicationDependencyContainer
import woowacourse.shopping.di.container.LifecycleDependencyContainer

abstract class DependencyInjectedApplication : Application() {
    lateinit var applicationDependencyContainer: LifecycleDependencyContainer
        private set

    override fun onCreate() {
        super.onCreate()
        applicationDependencyContainer = ApplicationDependencyContainer()
        applicationDependencyContainer.setApplicationContext(this)
    }
}
