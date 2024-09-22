package woowacourse.shopping

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.ComponentKey
import com.example.sh1mj1.component.InjectedComponent
import com.example.sh1mj1.component.InjectedSingletonContainer
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.DefaultInjectedSingletonContainer
import kotlin.reflect.KClass

class DefaultAppContainer(
    private val singletonComponentContainer: InjectedSingletonContainer = DefaultInjectedSingletonContainer.instance,
) : AppContainer {
    override fun add(component: InjectedComponent) {
        when (component) {
            is InjectedComponent.InjectedSingletonComponent -> singletonComponentContainer.add(component)
            is InjectedComponent.InjectedActivityComponent -> TODO()
        }
    }

    override fun add(vararg component: InjectedComponent) {
        component.forEach {
            add(it)
        }
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any = find(ComponentKey.of(clazz, qualifier))

    override fun find(componentKey: ComponentKey): Any = singletonComponentContainer.find(componentKey)

    override fun clearActivityScopedObjects() {
    }
}
