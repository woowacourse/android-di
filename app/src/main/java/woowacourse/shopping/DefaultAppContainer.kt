package woowacourse.shopping

import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.DefaultInjectedActivityContainer
import woowacourse.shopping.di.DefaultInjectedSingletonContainer
import woowacourse.shopping.di.InjectedActivityContainer
import woowacourse.shopping.di.InjectedComponent
import woowacourse.shopping.di.InjectedSingletonContainer
import woowacourse.shopping.di.Qualifier
import kotlin.reflect.KClass

class DefaultAppContainer(
    private val singletonComponentContainer: InjectedSingletonContainer = DefaultInjectedSingletonContainer,
    private val activityComponentContainer: InjectedActivityContainer = DefaultInjectedActivityContainer,
) : AppContainer {
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

    override fun find(clazz: KClass<*>): Any =
        singletonComponentContainer.find(clazz)
            ?: activityComponentContainer.find(clazz)
            ?: throw IllegalStateException("There is no component for ${clazz.simpleName}")

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier,
    ): Any =
        singletonComponentContainer.find(clazz, qualifier)
            ?: throw IllegalStateException("There is no component for ${clazz.simpleName}")

    override fun clearActivityScopedObjects() {
        activityComponentContainer.clear()
    }
}
