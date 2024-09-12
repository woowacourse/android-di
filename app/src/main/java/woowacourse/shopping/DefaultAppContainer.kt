package woowacourse.shopping

import com.example.sh1mj1.AppContainer
import com.example.sh1mj1.DefaultInjectedActivityContainer
import com.example.sh1mj1.DefaultInjectedSingletonContainer
import com.example.sh1mj1.InjectedActivityContainer
import com.example.sh1mj1.InjectedComponent
import com.example.sh1mj1.InjectedSingletonContainer
import com.example.sh1mj1.Qualifier
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
