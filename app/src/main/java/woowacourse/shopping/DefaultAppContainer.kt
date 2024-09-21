package woowacourse.shopping

import com.example.sh1mj1.AppContainer
import com.example.sh1mj1.ComponentKey
import com.example.sh1mj1.DefaultInjectedActivityContainer
import com.example.sh1mj1.DefaultInjectedSingletonContainer
import com.example.sh1mj1.InjectedActivityContainer
import com.example.sh1mj1.InjectedComponent
import com.example.sh1mj1.InjectedSingletonContainer

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

    override fun find(componentKey: ComponentKey): Any = singletonComponentContainer.findWithKey(componentKey)

    override fun clearActivityScopedObjects() {
        activityComponentContainer.clear()
    }
}
