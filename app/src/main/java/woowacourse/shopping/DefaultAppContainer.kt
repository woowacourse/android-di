package woowacourse.shopping

import woowacourse.di.InjectedActivityContainer
import woowacourse.di.InjectedComponent
import woowacourse.di.InjectedSingletonContainer
import kotlin.reflect.KClass

class DefaultAppContainer : AppContainer() {
    val singletonComponentContainer: InjectedSingletonContainer = InjectedSingletonContainer
    val activityComponentContainer: InjectedActivityContainer = InjectedActivityContainer

    override fun add(component: InjectedComponent) {
        when (component) {
            is InjectedComponent.InjectedSingletonComponent -> singletonComponentContainer.add(component)
            is InjectedComponent.InjectedActivityComponent -> activityComponentContainer.add(component)
        }
    }

    override fun find(clazz: KClass<*>): Any? =
        singletonComponentContainer.find(clazz) ?: activityComponentContainer.find(clazz)

    override fun clearActivityScopedObjects() {
        activityComponentContainer.clear()
    }
}
