package woowacourse.shopping.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.di.container.LifecycleDependencyContainer

abstract class DependencyInjectedActivity :
    AppCompatActivity() {
    lateinit var dependencyContainer: LifecycleDependencyContainer
    lateinit var dependencyInjector: LifecycleAwareDependencyInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dependencyContainer = (application as DependencyInjectedApplication).applicationDependencyContainer
        dependencyInjector = initializeDependencyInjector()
    }

    private fun initializeDependencyInjector(): LifecycleAwareDependencyInjector {
        return LifecycleAwareDependencyInjector().also {
            it.initDependencyContainer(dependencyContainer)
            it.initLifecycleOwner(this)
        }
    }
}
