package woowacourse.shopping.di

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.InjectedComponent
import com.example.sh1mj1.component.singleton.ComponentKey
import com.example.sh1mj1.container.singleton.InjectedSingletonContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

class FakeInjectedSingletonContainer(
    private val components: MutableList<InjectedComponent.InjectedSingletonComponent> = mutableListOf(),
) : InjectedSingletonContainer {
    override fun add(component: InjectedComponent.InjectedSingletonComponent) {
        components.add(component)
    }

    override fun find(
        clazz: KClass<*>,
        qualifier: Qualifier?,
    ): Any? =
        components.find { component ->
            clazz.isSuperclassOf(component.injectedClass) && qualifier.value == component.qualifier?.value
        }?.instance

    override fun find(componentKey: ComponentKey): Any {
        TODO("Not yet implemented")
    }
}
