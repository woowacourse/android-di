package woowacourse.shopping

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.ComponentKey
import com.example.sh1mj1.component.InjectedSingletonComponent
import com.example.sh1mj1.component.InjectedSingletonContainer
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.DefaultInjectedSingletonContainer
import kotlin.reflect.KClass

class DefaultAppContainer(
    private val singletonComponentContainer: InjectedSingletonContainer = DefaultInjectedSingletonContainer.instance,
) : AppContainer {
    override fun add(component: InjectedSingletonComponent) {
        singletonComponentContainer.add(component)
    }

    override fun add(vararg component: InjectedSingletonComponent) {
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
