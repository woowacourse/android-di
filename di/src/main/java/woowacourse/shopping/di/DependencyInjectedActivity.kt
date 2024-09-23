package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class DependencyInjectedActivity(application: DependencyInjectedApplication) :
    AppCompatActivity() {
    protected val dependencyContainer: LifecycleDependencyContainer =
        application.applicationDependencyContainer
    protected lateinit var dependencyInjector: LifecycleAwareDependencyInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyInjector = initializeDependencyInjector()
    }

    private fun initializeDependencyInjector(): LifecycleAwareDependencyInjector {
        return LifecycleAwareDependencyInjector().also {
            it.initDependencyContainer(dependencyContainer)
            it.initLifecycleOwner(this)
        }
    }
}
