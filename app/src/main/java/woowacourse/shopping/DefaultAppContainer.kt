package woowacourse.shopping

import woowacourse.di.AppContainer
import woowacourse.di.InjectedActivityContainer
import woowacourse.di.InjectedComponent
import woowacourse.di.InjectedSingletonContainer
import kotlin.reflect.KClass

class DefaultAppContainer : AppContainer {
    private val singletonComponentContainer: InjectedSingletonContainer = InjectedSingletonContainer
    private val activityComponentContainer: InjectedActivityContainer = InjectedActivityContainer

    override fun add(component: InjectedComponent) {
        when (component) {
            is InjectedComponent.InjectedSingletonComponent -> singletonComponentContainer.add(component)
            is InjectedComponent.InjectedActivityComponent -> activityComponentContainer.add(component)
        }
    }

    override fun add(vararg component: InjectedComponent) {
        component.forEach {
            add(it)
        }
    }

    override fun find(clazz: KClass<*>): Any? = singletonComponentContainer.find(clazz) ?: activityComponentContainer.find(clazz)

    override fun clearActivityScopedObjects() {
        activityComponentContainer.clear()
    }
}
